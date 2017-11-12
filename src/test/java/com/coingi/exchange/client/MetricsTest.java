package com.coingi.exchange.client;

import com.coingi.exchange.client.security.DefaultIdentityProvider;
import com.coingi.exchange.client.security.IncrementingNonceProvider;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class MetricsTest {

	@DataProvider
	public static Object[][] provideData() {
		return new Object[][]{
				{
						(Consumer<Coingi>) Coingi::getRates,
						true,
						"rates-ok"
				},
				{
						(Consumer<Coingi>) Coingi::getUserBalance,
						false,
						"user-balance-ok"
				}
		};
	}

	@Test
	@UseDataProvider("provideData")
	public void testSuccess(Consumer<Coingi> consumer, boolean publicCall, String responseName) {
		final Coingi coingi = new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {

			@Override
			protected Response callApi(final HttpRequestBase request) throws IOException {
				return ResponsesProvider.loadResponse(200, responseName);
			}

		};


		assertEquals(0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(0, coingi.getMetrics().getUserRequestCount());

		consumer.accept(coingi);

		assertEquals(publicCall ? 1 : 0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(publicCall ? 0 : 1, coingi.getMetrics().getUserRequestCount());
		assertEquals(1, coingi.getMetrics().getValidResponseCount());
		assertEquals(0, coingi.getMetrics().getErrorResponseCount());
		assertEquals(0, coingi.getMetrics().getRequestErrorCount());
	}

	@Test(expected = ApiCallException.class)
	@UseDataProvider("provideData")
	public void testErrors(Consumer<Coingi> consumer, boolean publicCall, String responseName) {
		final Coingi coingi = new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {

			@Override
			protected Response callApi(final HttpRequestBase request) throws IOException {
				return ResponsesProvider.loadResponse(400, "internal-error");
			}

		};

		assertEquals(0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(0, coingi.getMetrics().getUserRequestCount());

		consumer.accept(coingi);

		assertEquals(publicCall ? 1 : 0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(publicCall ? 0 : 1, coingi.getMetrics().getUserRequestCount());
		assertEquals(0, coingi.getMetrics().getValidResponseCount());
		assertEquals(1, coingi.getMetrics().getErrorResponseCount());
		assertEquals(0, coingi.getMetrics().getRequestErrorCount());
	}

	@Test(expected = ConnectionException.class)
	@UseDataProvider("provideData")
	public void unknownHostTest(Consumer<Coingi> consumer, boolean publicCall, String responseName) {
		final Coingi coingi = new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})).setHostname("invalid");

		assertEquals(0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(0, coingi.getMetrics().getUserRequestCount());

		try {
			consumer.accept(coingi);
		} catch (ConnectionException ex) {
			assertEquals(publicCall ? 1 : 0, coingi.getMetrics().getPublicRequestCount());
			assertEquals(publicCall ? 0 : 1, coingi.getMetrics().getUserRequestCount());
			assertEquals(0, coingi.getMetrics().getValidResponseCount());
			assertEquals(0, coingi.getMetrics().getErrorResponseCount());
			assertEquals(1, coingi.getMetrics().getRequestErrorCount());

			throw ex;
		}
	}

	@Test(expected = ConnectionException.class)
	@UseDataProvider("provideData")
	public void timeoutTest(Consumer<Coingi> consumer, boolean publicCall, String responseName) {
		final Coingi coingi = new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {

			@Override
			Response callApi(HttpRequestBase request) throws IOException {
				throw new SocketTimeoutException();
			}

		};

		assertEquals(0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(0, coingi.getMetrics().getUserRequestCount());

		try {
			consumer.accept(coingi);
		} catch (ConnectionException ex) {
			assertEquals(publicCall ? 1 : 0, coingi.getMetrics().getPublicRequestCount());
			assertEquals(publicCall ? 0 : 1, coingi.getMetrics().getUserRequestCount());
			assertEquals(0, coingi.getMetrics().getValidResponseCount());
			assertEquals(0, coingi.getMetrics().getErrorResponseCount());
			assertEquals(1, coingi.getMetrics().getRequestErrorCount());

			throw ex;
		}
	}

	@Test(expected = ConnectionException.class)
	@UseDataProvider("provideData")
	public void emptyResponseTest(Consumer<Coingi> consumer, boolean publicCall, String responseName) {
		final Coingi coingi = new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {

			@Override
			protected Response callApi(final HttpRequestBase request) {
				return new Response(400, "");
			}

		};

		assertEquals(0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(0, coingi.getMetrics().getUserRequestCount());

		try {
			consumer.accept(coingi);
		} catch (ConnectionException ex) {
			assertEquals(publicCall ? 1 : 0, coingi.getMetrics().getPublicRequestCount());
			assertEquals(publicCall ? 0 : 1, coingi.getMetrics().getUserRequestCount());
			assertEquals(0, coingi.getMetrics().getValidResponseCount());
			assertEquals(0, coingi.getMetrics().getErrorResponseCount());
			assertEquals(1, coingi.getMetrics().getRequestErrorCount());

			throw ex;
		}
	}

	@Test(expected = ConnectionException.class)
	@UseDataProvider("provideData")
	public void invalidResponseTest(Consumer<Coingi> consumer, boolean publicCall, String responseName) {
		final Coingi coingi = new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {

			@Override
			protected Response callApi(final HttpRequestBase request) {
				return new Response(400, "NOTJSON");
			}

		};

		assertEquals(0, coingi.getMetrics().getPublicRequestCount());
		assertEquals(0, coingi.getMetrics().getUserRequestCount());

		try {
			consumer.accept(coingi);
		} catch (ConnectionException ex) {
			assertEquals(publicCall ? 1 : 0, coingi.getMetrics().getPublicRequestCount());
			assertEquals(publicCall ? 0 : 1, coingi.getMetrics().getUserRequestCount());
			assertEquals(0, coingi.getMetrics().getValidResponseCount());
			assertEquals(0, coingi.getMetrics().getErrorResponseCount());
			assertEquals(1, coingi.getMetrics().getRequestErrorCount());

			throw ex;
		}
	}

}
