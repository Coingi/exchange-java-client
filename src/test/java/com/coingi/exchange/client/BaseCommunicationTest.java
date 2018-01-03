package com.coingi.exchange.client;

import com.coingi.exchange.client.entities.Currency;
import com.coingi.exchange.client.entities.*;
import com.tngtech.java.junit.dataprovider.DataProvider;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public abstract class BaseCommunicationTest {

	@DataProvider
	public static Object[][] providePublicMethodsData() {
		return new Object[][]{
				{
						"https://api.coingi.com/current/rates",
						"rates-ok",
						(Function<Coingi, List<CurrencyPairRate>>) Coingi::getRates,
						ResponsesProvider.getExpectedRates()
				},
				{
						"https://api.coingi.com/current/order-book/ltc-btc/50/50/10",
						"order-book-ok",
						(Function<Coingi, OrderBook>) coingi -> coingi.getOrderBook(CurrencyPair.LTC_BTC),
						ResponsesProvider.getExpectedOrderBook()
				},
				{
						"https://api.coingi.com/current/order-book/ltc-btc/10/20/30",
						"order-book-ok",
						(Function<Coingi, OrderBook>) coingi -> coingi.getOrderBook(CurrencyPair.LTC_BTC, 10, 20, 30),
						ResponsesProvider.getExpectedOrderBook()
				},
				{
						"https://api.coingi.com/current/transactions/ltc-btc/50",
						"transactions-ok",
						(Function<Coingi, List<Transaction>>) coingi -> coingi.getTransactions(CurrencyPair.LTC_BTC),
						ResponsesProvider.getExpectedTransactions()
				},
				{
						"https://api.coingi.com/current/transactions/ltc-btc/123",
						"transactions-ok",
						(Function<Coingi, List<Transaction>>) coingi -> coingi.getTransactions(CurrencyPair.LTC_BTC, 123),
						ResponsesProvider.getExpectedTransactions()
				},
				{
						"https://api.coingi.com/current/24hour-rolling-aggregation",
						"24hour-rolling-aggregation",
						(Function<Coingi, List<RollingAggregation>>) Coingi::getRollingAggregations,
						ResponsesProvider.getExpectedRollingAggregations()
				},
		};
	}

	@DataProvider
	public static List<List<Object>> providePrivateReadOnlyMethodsData() {
		final List<List<Object>> data = new ArrayList<>();

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("currencies", Currency.BTC.identifier + "," + Currency.LTC.identifier + "," + Currency.PPC.identifier + "," + Currency.DOGE.identifier + "," + Currency.VTC.identifier + "," + Currency.NMC.identifier + "," + Currency.DASH.identifier + "," + Currency.USD.identifier + "," + Currency.EUR.identifier);

			data.add(Arrays.asList(
					"https://api.coingi.com/user/balance",
					"user-balance-ok",
					(Function<Coingi, List<UserBalance>>) Coingi::getUserBalance,
					expectedParameters,
					ResponsesProvider.getExpectedUserBalances()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("currencies", Currency.BTC.identifier + "," + Currency.LTC.identifier);

			data.add(Arrays.asList(
					"https://api.coingi.com/user/balance",
					"user-balance-ok",
					(Function<Coingi, List<UserBalance>>) coingi -> coingi.getUserBalance(Currency.BTC, Currency.LTC),
					expectedParameters,
					ResponsesProvider.getExpectedUserBalances()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");
			expectedParameters.put("type", String.valueOf(OrderType.ASK.type));

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1, OrderType.ASK),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");
			expectedParameters.put("currencyPair", CurrencyPair.LTC_BTC.toString());

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1, CurrencyPair.LTC_BTC),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");
			expectedParameters.put("status", String.valueOf(OrderStatus.ACTIVE.value));

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1, OrderStatus.ACTIVE),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");
			expectedParameters.put("type", String.valueOf(OrderType.ASK.type));
			expectedParameters.put("currencyPair", CurrencyPair.LTC_BTC.toString());

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1, OrderType.ASK, CurrencyPair.LTC_BTC),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");
			expectedParameters.put("type", String.valueOf(OrderType.ASK.type));
			expectedParameters.put("status", String.valueOf(OrderStatus.ACTIVE.value));

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1, OrderType.ASK, OrderStatus.ACTIVE),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");
			expectedParameters.put("currencyPair", CurrencyPair.LTC_BTC.toString());
			expectedParameters.put("status", String.valueOf(OrderStatus.ACTIVE.value));

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1, CurrencyPair.LTC_BTC, OrderStatus.ACTIVE),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("pageNumber", "1");
			expectedParameters.put("pageSize", "1");
			expectedParameters.put("type", String.valueOf(OrderType.ASK.type));
			expectedParameters.put("currencyPair", CurrencyPair.LTC_BTC.toString());
			expectedParameters.put("status", String.valueOf(OrderStatus.ACTIVE.value));

			data.add(Arrays.asList(
					"https://api.coingi.com/user/orders",
					"user-orders-ok",
					(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1, OrderType.ASK, CurrencyPair.LTC_BTC, OrderStatus.ACTIVE),
					expectedParameters,
					ResponsesProvider.getExpectedOrdersList()
			));
		}

		return data;
	}

	@DataProvider
	public static List<List<Object>> providePrivateMethodsData() {
		final List<List<Object>> data = new ArrayList<>();


		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("currencyPair", "ltc-btc");
			expectedParameters.put("type", "1");
			expectedParameters.put("price", "12.34");
			expectedParameters.put("volume", "56.78");

			data.add(Arrays.asList(
					"https://api.coingi.com/user/add-order",
					"add-order-ok",
					(Function<Coingi, UUID>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, OrderType.ASK, BigDecimal.valueOf(1234, 2), BigDecimal.valueOf(5678, 2)),
					expectedParameters,
					ResponsesProvider.getExpectedOrderId()
			));
		}

		{
			final Map<String, String> expectedParameters = new HashMap<>();
			expectedParameters.put("orderId", "11e7ab3e-a61e-9f14-9ca9-02429735e3c2");

			data.add(Arrays.asList(
					"https://api.coingi.com/user/cancel-order",
					"cancel-order-ok",
					(Function<Coingi, Order>) coingi -> coingi.cancelOrder(UUID.fromString("11e7ab3e-a61e-9f14-9ca9-02429735e3c2")),
					expectedParameters,
					ResponsesProvider.getExpectedCanceledOrder()
			));
		}


		return data;
	}

	@DataProvider
	public static Object[][] provideMethodErrorsData() {
		return new Object[][]{
				{
						"order-book-error",
						(Function<Coingi, OrderBook>) coingi -> coingi.getOrderBook(CurrencyPair.LTC_BTC),
						ResponsesProvider.getExpectedOrderBookException()
				},
				{
						"transactions-error",
						(Function<Coingi, List<Transaction>>) coingi -> coingi.getTransactions(CurrencyPair.LTC_BTC),
						ResponsesProvider.getExpectedTransactionsException()
				},
				{
						"user-balance-error",
						(Function<Coingi, List<UserBalance>>) Coingi::getUserBalance,
						ResponsesProvider.getExpectedUserBalanceException()
				},
				{
						"add-order-error",
						(Function<Coingi, UUID>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, OrderType.ASK, BigDecimal.valueOf(1234, 2), BigDecimal.valueOf(5678, 2)),
						ResponsesProvider.getExpectedAddOrderException()
				},
				{
						"cancel-order-error",
						(Function<Coingi, Order>) coingi -> coingi.cancelOrder(UUID.fromString("11e7ab3e-a61e-9f14-9ca9-02429735e3c2")),
						ResponsesProvider.getExpectedCancelOrderException()
				},
				{
						"user-orders-error",
						(Function<Coingi, OrdersList>) coingi -> coingi.getOrders(1, 1),
						ResponsesProvider.getExpectedGetOrdersException()
				},
		};
	}

	static void assertFunctionExceptionEquals(ApiCallException expected, ApiCallException tested) {
		assertEquals(expected.getStatusCode(), tested.getStatusCode());
		assertEquals(new ArrayList<>(expected.getErrors()), new ArrayList<>(tested.getErrors()));
	}

}
