package com.coingi.exchange.client;

import com.coingi.exchange.client.entities.Currency;
import com.coingi.exchange.client.entities.CurrencyPair;
import com.coingi.exchange.client.entities.OrderStatus;
import com.coingi.exchange.client.entities.OrderType;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(DataProviderRunner.class)
public class ParameterValidationTest {

	private enum VALIDATION_ERROR {
		NULL_POINTER_EXCEPTION, ASSERTION
	}


	@DataProvider
	public static Object[][] provideInputErrorData() {
		return new Object[][]{
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrderBook(null),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrderBook(null, 1, 1, 1),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrderBook(CurrencyPair.LTC_BTC, 0, 1, 1),
						VALIDATION_ERROR.ASSERTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrderBook(CurrencyPair.LTC_BTC, 1, 0, 1),
						VALIDATION_ERROR.ASSERTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrderBook(CurrencyPair.LTC_BTC, 1, 1, 0),
						VALIDATION_ERROR.ASSERTION
				},
				//
				{
						(Function<Coingi, Object>) coingi -> coingi.getTransactions(null),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getTransactions(null, 1),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getTransactions(CurrencyPair.LTC_BTC, 0),
						VALIDATION_ERROR.ASSERTION
				},
				//
				{
						(Function<Coingi, Object>) coingi -> coingi.getUserBalance(new Currency[]{}),
						VALIDATION_ERROR.ASSERTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getUserBalance(new Currency[]{Currency.BTC, null}),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				//
				{
						(Function<Coingi, Object>) coingi -> coingi.addOrder(null, OrderType.ASK, BigDecimal.valueOf(1234, 2), BigDecimal.valueOf(5678, 2)),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, null, BigDecimal.valueOf(1234, 2), BigDecimal.valueOf(5678, 2)),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, OrderType.ASK, null, BigDecimal.valueOf(5678, 2)),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, OrderType.ASK, BigDecimal.ZERO, BigDecimal.valueOf(5678, 2)),
						VALIDATION_ERROR.ASSERTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, OrderType.ASK, BigDecimal.valueOf(1234, 2), null),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, OrderType.ASK, BigDecimal.valueOf(1234, 2), BigDecimal.ZERO),
						VALIDATION_ERROR.ASSERTION
				},
				//
				{
						(Function<Coingi, Object>) coingi -> coingi.cancelOrder(null),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				//
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrders(0, 1),
						VALIDATION_ERROR.ASSERTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrders(1, 0),
						VALIDATION_ERROR.ASSERTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrders(1, 1, null, CurrencyPair.LTC_BTC, OrderStatus.ACTIVE),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrders(1, 1, OrderType.ASK, null, OrderStatus.ACTIVE),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrders(1, 1, OrderType.ASK, CurrencyPair.LTC_BTC, null),
						VALIDATION_ERROR.NULL_POINTER_EXCEPTION
				}
		};
	}

	@Test
	@UseDataProvider("provideInputErrorData")
	public void testInputError(Function<Coingi, ?> callback, ParameterValidationTest.VALIDATION_ERROR expectedError) {
		final Coingi client = new DummyClient();

		boolean failed = false;
		try {
			callback.apply(client);
		} catch (Throwable ex) {
			failed = true;
			switch (expectedError) {
				case NULL_POINTER_EXCEPTION:
					assertThat(ex, instanceOf(NullPointerException.class));
					break;

				case ASSERTION:
					assertThat(ex, instanceOf(AssertionError.class));
					break;

				default:
					throw ex;
			}
		}

		if (!failed) {
			Assert.fail("Client invocation did not fail.");
		}
	}


}

