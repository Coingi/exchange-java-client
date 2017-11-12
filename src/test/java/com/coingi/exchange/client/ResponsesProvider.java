package com.coingi.exchange.client;

import com.coingi.exchange.client.entities.*;
import com.coingi.exchange.client.entities.Error;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class ResponsesProvider {

	static List<CurrencyPairRate> getExpectedRates() {
		return Arrays.asList(
				new CurrencyPairRate(CurrencyPair.VTC_BTC, BigDecimal.valueOf(31, 5)),
				new CurrencyPairRate(CurrencyPair.LTC_BTC, BigDecimal.valueOf(115, 4))
		);
	}

	static ApiCallException getExpectedOrderBookException() {
		return new ApiCallException(
				400,
				Arrays.asList(
						new Error(100001, "Parameter \"currencyPair\" value is missing. Expecting currency pair definition."),
						new Error(100011, "Parameter \"maxAskCount\" value is missing. Expecting integer."),
						new Error(100021, "Parameter \"maxBidCount\" value is missing. Expecting integer."),
						new Error(100031, "Parameter \"maxDepthRangeCount\" value is missing. Expecting integer.")
				)
		);
	}

	static OrderBook getExpectedOrderBook() {
		return new OrderBook(
				Arrays.asList(
						new OrderGroup(
								OrderType.ASK,
								CurrencyPair.LTC_BTC,
								BigDecimal.valueOf(17, 3),
								BigDecimal.valueOf(32501, 3),
								BigDecimal.valueOf(552517, 6)
						),
						new OrderGroup(
								OrderType.ASK,
								CurrencyPair.LTC_BTC,
								BigDecimal.valueOf(1998, 5),
								BigDecimal.valueOf(23, 3),
								BigDecimal.valueOf(45954, 8)
						)
				),
				Arrays.asList(
						new OrderGroup(
								OrderType.BID,
								CurrencyPair.LTC_BTC,
								BigDecimal.valueOf(1681, 5),
								BigDecimal.valueOf(2922, 2),
								BigDecimal.valueOf(4911882, 7)
						),
						new OrderGroup(
								OrderType.BID,
								CurrencyPair.LTC_BTC,
								BigDecimal.valueOf(167, 4),
								BigDecimal.valueOf(1285, 2),
								BigDecimal.valueOf(214595, 6)
						)
				),
				Arrays.asList(
						new DepthRange(
								BigDecimal.valueOf(17, 3), BigDecimal.valueOf(32501, 3)
						),
						new DepthRange(
								BigDecimal.valueOf(195, 4), BigDecimal.valueOf(32524, 3)
						)
				),
				Arrays.asList(
						new DepthRange(
								BigDecimal.valueOf(165, 4), BigDecimal.valueOf(4207, 2)
						),
						new DepthRange(
								BigDecimal.valueOf(16, 3), BigDecimal.valueOf(4662, 2)
						)
				)
		);
	}

	static List<Transaction> getExpectedTransactions() {
		return Arrays.asList(
				new Transaction(
						UUID.fromString("11e737e9-b60e-8240-b34a-0059bb86f406"),
						1494686258000L,
						CurrencyPair.LTC_BTC,
						BigDecimal.valueOf(15876, 3),
						BigDecimal.valueOf(1612, 5),
						OrderType.BID
				),
				new Transaction(
						UUID.fromString("11e737e7-c423-82ba-86d2-0059bb86f406"),
						1494685423000L,
						CurrencyPair.LTC_BTC,
						BigDecimal.valueOf(594, 2),
						BigDecimal.valueOf(1614, 5),
						OrderType.BID
				)
		);
	}

	static ApiCallException getExpectedTransactionsException() {
		return new ApiCallException(
				400,
				Arrays.asList(
						new Error(200001, "Parameter \"currencyPair\" value is missing. Expecting currency pair definition."),
						new Error(200011, "Parameter \"limit\" value is missing. Expecting integer.")
				)
		);
	}

	static List<RollingAggregation> getExpectedRollingAggregations() {
		return Arrays.asList(
				new RollingAggregation(
						CurrencyPair.DOGE_BTC,
						BigDecimal.valueOf(67, 8),
						BigDecimal.valueOf(68, 8),
						BigDecimal.valueOf(67, 8),
						BigDecimal.valueOf(103114, 0),
						BigDecimal.valueOf(6934318, 8),
						BigDecimal.valueOf(68, 8),
						BigDecimal.valueOf(67, 8)
				),
				new RollingAggregation(
						CurrencyPair.LTC_BTC,
						BigDecimal.valueOf(1612, 5),
						BigDecimal.valueOf(1998, 5),
						BigDecimal.valueOf(1607, 5),
						BigDecimal.valueOf(602123, 3),
						BigDecimal.valueOf(966881908, 8),
						BigDecimal.valueOf(166, 4),
						BigDecimal.valueOf(1562, 5)
				)
		);
	}

	static UserBalancesList getExpectedUserBalances() {
		return new UserBalancesList(Arrays.asList(
				new UserBalance(
						Currency.BTC.getCurrencyObject(),
						BigDecimal.valueOf(34),
						BigDecimal.valueOf(965, 2),
						BigDecimal.ZERO,
						BigDecimal.valueOf(1, 1),
						BigDecimal.ZERO
				),
				new UserBalance(
						Currency.LTC.getCurrencyObject(),
						BigDecimal.valueOf(68, 1),
						BigDecimal.valueOf(32, 1),
						BigDecimal.ZERO,
						BigDecimal.ZERO,
						BigDecimal.ZERO
				)
		));
	}

	static ApiCallException getExpectedUserBalanceException() {
		return new ApiCallException(
				400,
				Arrays.asList(
						new Error(100700001, "Invalid value for parameter \"currencies\". Expected non empty string, found \"\".")
				)
		);
	}

	static UUID getExpectedOrderId() {
		return UUID.fromString("11e74215-7ea7-3c12-b8c1-02429735e3c2");
	}

	static ApiCallException getExpectedAddOrderException() {
		return new ApiCallException(
				400,
				Arrays.asList(
						new Error(100130005, "Parameter \"token\" value is missing. Expecting token/API key."),
						new Error(100100001, "Parameter \"currencyPair\" value is missing. Expecting currency pair definition."),
						new Error(100100011, "Parameter \"type\" value is missing. Expecting integer."),
						new Error(100100021, "Parameter \"volume\" value is missing. Expecting floating point number."),
						new Error(100100031, "Parameter \"price\" value is missing. Expecting floating point number.")
				)
		);
	}

	static ApiCallException getExpectedGetOrdersException() {
		return new ApiCallException(
				400,
				Arrays.asList(
						new Error(100300002, "Invalid value for parameter \"pageNumber\". Expected range between 1 and 100, found \"32767\"."),
						new Error(100300012, "Invalid value for parameter \"pageSize\". Expected range between 1 and 100, found \"32767\".")
				)
		);
	}

	static Order getExpectedCanceledOrder() {
		return new Order(
				UUID.fromString("11e7ab3e-a61e-9f14-9ca9-02429735e3c2"),
				OrderType.BID,
				1507367122,
				CurrencyPair.LTC_BTC,
				BigDecimal.valueOf(1, 0),
				BigDecimal.valueOf(4, 1),
				BigDecimal.valueOf(4, 1),
				BigDecimal.valueOf(7, 1),
				BigDecimal.valueOf(7, 1),
				OrderStatus.PARTIALLY_PROCESSED
		);
	}

	static OrdersList getExpectedOrdersList() {
		return new OrdersList(
				true,
				Arrays.asList(
						new Order(
								UUID.fromString("11e7ab3e-a61e-9f14-9ca9-02429735e3c2"),
								OrderType.BID,
								1507367122000L,
								CurrencyPair.LTC_BTC,
								BigDecimal.valueOf(1, 0),
								BigDecimal.valueOf(7, 1),
								BigDecimal.valueOf(7, 1),
								BigDecimal.valueOf(7, 1),
								BigDecimal.valueOf(7, 1),
								OrderStatus.PARTIALLY_PROCESSED
						),
						new Order(
								UUID.fromString("11e7ab3e-a553-aaac-bab6-02429735e3c2"),
								OrderType.BID,
								1507367121000L,
								CurrencyPair.LTC_BTC,
								BigDecimal.valueOf(1, 0),
								BigDecimal.valueOf(7, 1),
								BigDecimal.valueOf(7, 1),
								BigDecimal.valueOf(7, 1),
								BigDecimal.valueOf(7, 1),
								OrderStatus.ACTIVE
						)
				)
		);
	}

	static ApiCallException getExpectedCancelOrderException() {
		return new ApiCallException(
				400,
				Collections.singletonList(
						new Error(100200003, "Order is alrady cancelled.")
				)
		);
	}

	static Coingi.Response loadResponse(int status, String name) throws IOException {
		return new Coingi.Response(status, loadResponse(name));
	}

	static String loadResponse(String name) throws IOException {
		try (final BufferedReader r = new BufferedReader(new InputStreamReader(ResponsesProcessingTest.class.getClassLoader().getResourceAsStream("responses/" + name + ".json")))) {
			return r.lines().collect(Collectors.joining("\n"));
		}
	}

}
