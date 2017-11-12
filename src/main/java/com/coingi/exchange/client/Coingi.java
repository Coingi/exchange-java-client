package com.coingi.exchange.client;

import com.coingi.exchange.client.entities.Currency;
import com.coingi.exchange.client.entities.*;
import com.coingi.exchange.client.entities.Error;
import com.coingi.exchange.client.security.IdentityProvider;
import com.coingi.exchange.client.security.NonceProvider;
import com.coingi.exchange.client.utils.UrlBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.*;

public class Coingi {

	final private static String DEFAULT_HOSTNAME = "api.coingi.com";

	final private static int DEFAULT_TIMEOUT = 5000;

	private final Logger logger = LogManager.getLogger(Coingi.class);

	private final Metrics metrics = new Metrics();

	private CloseableHttpClient client;

	private String hostname;

	private String entryPoint;

	private int timeout = DEFAULT_TIMEOUT;

	private final NonceProvider nonceProvider;

	private final IdentityProvider identityProvider;

	/**
	 * Creates a client for public functions only.
	 */
	public Coingi() {
		this.nonceProvider = null;
		this.identityProvider = null;

		initializeDefaults();
	}

	/**
	 * Creates a client for both public and private functions.
	 *
	 * @param nonceProvider    Nonce provider
	 * @param identityProvider Identity provider
	 */
	public Coingi(NonceProvider nonceProvider, IdentityProvider identityProvider) {
		this.nonceProvider = Objects.requireNonNull(nonceProvider);
		this.identityProvider = Objects.requireNonNull(identityProvider);

		initializeDefaults();
	}

	private void initializeDefaults() {
		this.hostname = DEFAULT_HOSTNAME;
		this.entryPoint = createEntryPoint(this.hostname);
		configureHttpClient(getDefaultHttpClientBuilder());
	}

	/**
	 * Returns current prices for all supported currency pairs.
	 *
	 * @return Current prices
	 */
	public List<CurrencyPairRate> getRates() {
		return callPublicFunction("rates", CurrencyPairRates.class).getList();
	}

	/**
	 * Returns the current order book for the given currency with up to 50 asks, bids and up to 10 depth ranges.
	 *
	 * @param currencyPair Currency pair
	 * @return Current order book
	 */
	public OrderBook getOrderBook(CurrencyPair currencyPair) {
		return getOrderBook(currencyPair, 50, 50, 10);
	}

	/**
	 * Returns the current order book.
	 *
	 * @param currencyPair       Currency pair
	 * @param maxAskCount        Maximum number of asks
	 * @param maxBidCount        Maximum number of bids
	 * @param maxDepthRangeCount Maximum number of depth ranges
	 * @return Current order book
	 */
	public OrderBook getOrderBook(CurrencyPair currencyPair, int maxAskCount, int maxBidCount, int maxDepthRangeCount) {
		assert maxAskCount > 0;
		assert maxBidCount > 0;
		assert maxDepthRangeCount > 0;

		return callPublicFunction("order-book", OrderBook.class, Objects.requireNonNull(currencyPair), maxAskCount, maxBidCount, maxDepthRangeCount);
	}

	/**
	 * Returns a list of up to 50 latest transactions.
	 *
	 * @param currencyPair Currency pair
	 * @return A list of latest transactions
	 */
	public List<Transaction> getTransactions(CurrencyPair currencyPair) {
		return getTransactions(currencyPair, 50);
	}

	/**
	 * Returns a list of latest transactions.
	 *
	 * @param currencyPair Currency pair
	 * @param maxCount     Maximum number of transactions
	 * @return A list of latest transactions
	 */
	public List<Transaction> getTransactions(CurrencyPair currencyPair, int maxCount) {
		assert maxCount > 0;

		return callPublicFunction("transactions", TransactionsList.class, Objects.requireNonNull(currencyPair), maxCount).getList();
	}

	/**
	 * Returns a list of 24hr rolling aggregations for all supported currency pairs.
	 *
	 * @return A list of aggregations
	 */
	public List<RollingAggregation> getRollingAggregations() {
		return callPublicFunction("24hour-rolling-aggregation", RollingAggregationsList.class).getList();
	}

	/**
	 * Returns user balance for all supported currencies.
	 *
	 * @return A list of user balances
	 */
	public List<UserBalance> getUserBalance() {
		return getUserBalance(Currency.values());
	}

	/**
	 * Returns user balance for each provided currency.
	 *
	 * @param currencies Currencies
	 * @return A list of user balances
	 */
	public List<UserBalance> getUserBalance(Currency... currencies) {
		assert currencies.length > 0;

		final StringBuilder currenciesBuilder = new StringBuilder();
		for (Currency currency : currencies) {
			currenciesBuilder.append(Objects.requireNonNull(currency).identifier);
			currenciesBuilder.append(',');
		}
		currenciesBuilder.setLength(currenciesBuilder.length() - 1);

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("currencies", currenciesBuilder.toString());

		return callUserFunction("balance", UserBalancesList.class, parameters).getList();
	}

	/**
	 * Creates a new order.
	 *
	 * @param currencyPair Currency pair
	 * @param type         Order type
	 * @param price        Order value
	 * @param volume       Order volume
	 * @return Order UUID
	 */
	public UUID addOrder(CurrencyPair currencyPair, OrderType type, BigDecimal price, BigDecimal volume) {
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("currencyPair", Objects.requireNonNull(currencyPair).toString());
		parameters.put("type", Objects.requireNonNull(type).type);

		assert Objects.requireNonNull(price).compareTo(BigDecimal.ZERO) > 0;
		parameters.put("price", price.doubleValue());

		assert Objects.requireNonNull(volume).compareTo(BigDecimal.ZERO) > 0;
		parameters.put("volume", volume.doubleValue());

		return callUserFunction("add-order", UUIDResult.class, parameters).getResult();
	}

	/**
	 * Cancels the given order.
	 *
	 * @param id Order ID
	 * @return Cancelled order description
	 */
	public Order cancelOrder(UUID id) {
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("orderId", Objects.requireNonNull(id));

		return callUserFunction("cancel-order", Order.class, parameters);
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page     Page number
	 * @param pageSize Page size
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize) {
		return doGetOrders(page, pageSize, null, null, null);
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page     Page number
	 * @param pageSize Page size
	 * @param type     Order type (NULL for all types)
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize, OrderType type) {
		return doGetOrders(page, pageSize, Objects.requireNonNull(type), null, null);
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page         Page number
	 * @param pageSize     Page size
	 * @param currencyPair Currency pair (NULL for all currency pairs)
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize, CurrencyPair currencyPair) {
		return doGetOrders(page, pageSize, null, Objects.requireNonNull(currencyPair), null);
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page     Page number
	 * @param pageSize Page size
	 * @param status   Order status (NULL for all states)
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize, OrderStatus status) {
		return doGetOrders(page, pageSize, null, null, Objects.requireNonNull(status));
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page         Page number
	 * @param pageSize     Page size
	 * @param type         Order type (NULL for all types)
	 * @param currencyPair Currency pair (NULL for all currency pairs)
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize, OrderType type, CurrencyPair currencyPair) {
		return doGetOrders(page, pageSize, Objects.requireNonNull(type), Objects.requireNonNull(currencyPair), null);
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page     Page number
	 * @param pageSize Page size
	 * @param type     Order type (NULL for all types)
	 * @param status   Order status (NULL for all states)
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize, OrderType type, OrderStatus status) {
		return doGetOrders(page, pageSize, Objects.requireNonNull(type), null, Objects.requireNonNull(status));
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page         Page number
	 * @param pageSize     Page size
	 * @param currencyPair Currency pair (NULL for all currency pairs)
	 * @param status       Order status (NULL for all states)
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize, CurrencyPair currencyPair, OrderStatus status) {
		return doGetOrders(page, pageSize, null, Objects.requireNonNull(currencyPair), Objects.requireNonNull(status));
	}

	/**
	 * Returns a collection of user orders.
	 *
	 * @param page         Page number
	 * @param pageSize     Page size
	 * @param type         Order type (NULL for all types)
	 * @param currencyPair Currency pair (NULL for all currency pairs)
	 * @param status       Order status (NULL for all states)
	 * @return A collection of orders
	 */
	public OrdersList getOrders(int page, int pageSize, OrderType type, CurrencyPair currencyPair, OrderStatus status) {
		return doGetOrders(page, pageSize, Objects.requireNonNull(type), Objects.requireNonNull(currencyPair), Objects.requireNonNull(status));
	}

	OrdersList doGetOrders(int page, int pageSize, OrderType type, CurrencyPair currencyPair, OrderStatus status) {
		assert page > 0;
		assert pageSize > 0;

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("pageNumber", page);
		parameters.put("pageSize", pageSize);
		if (type != null) {
			parameters.put("type", type.type);
		}
		if (currencyPair != null) {
			parameters.put("currencyPair", currencyPair.toString());
		}
		if (status != null) {
			parameters.put("status", status.value);
		}

		return callUserFunction("orders", OrdersList.class, parameters);
	}


	<T> T callPublicFunction(String functionName, Class<T> responseType, Object... parameters) {
		try {
			final T result = extractResult(callPublicFunction(functionName, parameters), responseType);

			metrics.registerResponse(true);

			return result;
		} catch (ClientException ex) {
			metrics.registerResponse(false);

			throw ex;
		} catch (UnknownHostException ex) {
			metrics.registerRequestError();

			throw new ConnectionException("Invalid API hostname.", ex);
		} catch (SocketTimeoutException ex) {
			metrics.registerRequestError();

			throw new ConnectionException("Connection timeout.", ex);
		} catch (IOException ex) {
			metrics.registerRequestError();

			throw new ConnectionException("Could not execute the function.", ex);
		}
	}


	<T> T callUserFunction(String functionName, Class<T> responseType, Map<String, Object> parameters) {
		try {
			final T result = extractResult(callUserFunction(functionName, parameters), responseType);

			metrics.registerResponse(true);

			return result;
		} catch (ClientException ex) {
			metrics.registerResponse(false);

			throw ex;
		} catch (UnknownHostException ex) {
			metrics.registerRequestError();

			throw new ConnectionException("Invalid API hostname.", ex);
		} catch (SocketTimeoutException ex) {
			metrics.registerRequestError();

			throw new ConnectionException("Connection timeout.", ex);
		} catch (IOException ex) {
			metrics.registerRequestError();

			throw new ConnectionException("Could not execute the function.", ex);
		}
	}

	Response callPublicFunction(String functionName, Object... parameters) throws IOException {
		metrics.registerPublicRequest();

		final String url = parameters.length > 0 ?
				String.format("%s/current/%s/%s", entryPoint, functionName, UrlBuilder.buildUrlPath(parameters)) :
				String.format("%s/current/%s", entryPoint, functionName);

		return callApi(new HttpGet(url));
	}

	Response callUserFunction(String functionName, Map<String, Object> parameters) throws IOException {
		if (nonceProvider == null || identityProvider == null) {
			throw new ClientException("This client can access only public functions.");
		}

		metrics.registerUserRequest();

		final String url = String.format("%s/user/%s", entryPoint, functionName);
		final long nonce = nonceProvider.get();

		final Map<String, Object> completeParameters = new HashMap<>(parameters);
		completeParameters.put("nonce", nonce);
		completeParameters.put("token", new String(identityProvider.getPublicKey(), Charset.forName("US-ASCII")));
		completeParameters.put("signature", identityProvider.getSignature(nonce));

		return callPrivateFunction(url, completeParameters);
	}

	Response callPrivateFunction(String urlPath, Map<String, Object> parameters) throws IOException {
		final HttpPost request = new HttpPost(Objects.requireNonNull(urlPath));
		request.setEntity(new StringEntity(Codec.encode(Objects.requireNonNull(parameters)), ContentType.APPLICATION_JSON));

		return callApi(request);
	}

	String createEntryPoint(String hostname) {
		return String.format("https://%s", hostname);
	}

	<T> T extractResult(Response responseData, Class<T> resultType) throws IOException {
		try {
			if (responseData.statusCode == 200) {
				return Codec.decode(responseData.response, resultType);
			} else {
				final ErrorContainer errorContainer = Codec.decode(responseData.response, ErrorContainer.class);

				if (errorContainer == null) {
					throw new IOException("Empty server response.");
				} else {
					throw new ApiCallException(responseData.statusCode, errorContainer.getErrors());
				}
			}
		} catch (JsonSyntaxException ex) {
			throw new IOException("Could not parse the server response.", ex);
		}
	}

	Response callApi(HttpRequestBase request) throws IOException {
		logger.debug("Sending a {} request to {}.", request.getMethod(), request.getURI());

		try (final CloseableHttpResponse response = client.execute(configureRequest(request)); final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			logger.trace("Received {} response to {} request to {}.", response.getStatusLine().getStatusCode(), request.getMethod(), request.getURI());

			response.getEntity().writeTo(output);

			return new Response(response.getStatusLine().getStatusCode(), new String(output.toByteArray(), "UTF-8"));
		}
	}

	/**
	 * Performs additional request configuration just before the requests gets executed.
	 *
	 * @param request Executed request
	 * @return configured request
	 */
	HttpUriRequest configureRequest(HttpRequestBase request) {
		final RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.setConnectTimeout(timeout)
				.build();
		request.setConfig(config);

		return request;
	}

	/**
	 * Configures the HTTP client used to communicate with CoingiTrader.com.
	 *
	 * @param builder Client builder
	 * @return fluent interface
	 */
	Coingi configureHttpClient(final HttpClientBuilder builder) {
		try {
			if (client != null) {
				client.close();
			}
		} catch (IOException e) {
			// NO OP, not much to do here
		}

		client = builder.build();

		return this;
	}

	/**
	 * Returns the configured request timeout.
	 *
	 * @return Timeout (in milliseconds)
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Sets request timeout.
	 *
	 * @param timeout Request timeout (in milliseconds)
	 * @return Fluent interface
	 */
	public Coingi setTimeout(int timeout) {
		this.timeout = timeout;

		return this;
	}

	/**
	 * Returns the configured API hostname.
	 *
	 * @return API hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets the API hostname.
	 *
	 * @param hostname API hostname
	 * @return Fluent interface
	 */
	public Coingi setHostname(String hostname) {
		this.hostname = Objects.requireNonNull(hostname);
		this.entryPoint = createEntryPoint(hostname);

		logger.info("API hostname set to {} setting entry point to {}.", hostname, entryPoint);

		return this;
	}

	/**
	 * Returns a default HTTP client builder.
	 *
	 * @return the default HTTP client builder
	 */
	public static HttpClientBuilder getDefaultHttpClientBuilder() {
		return HttpClientBuilder.create()
				.setConnectionManager(new PoolingHttpClientConnectionManager())
				.setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.disableRedirectHandling();
	}

	/**
	 * Returns basic metrics.
	 *
	 * @return Metrics
	 */
	public Metrics getMetrics() {
		return metrics;
	}

	static class Response {

		final int statusCode;

		final String response;

		Response(int statusCode, String response) {
			this.statusCode = statusCode;
			this.response = response;
		}

	}

	static class ErrorContainer {

		private List<Error> errors;

		List<Error> getErrors() {
			return errors == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(errors);
		}

	}

	private final static class Codec {

		private final static Gson encoder = new Gson();

		private final static Gson decoder = new Gson();

		synchronized static String encode(Map<String, Object> data) {
			return encoder.toJson(data);
		}

		static <T> T decode(String json, Class<T> resultType) {
			synchronized (decoder) {
				return decoder.fromJson(json, resultType);
			}
		}

		static <T> T decode(String json, Type resultType) {
			synchronized (decoder) {
				return decoder.fromJson(json, resultType);
			}
		}

	}

}
