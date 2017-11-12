package com.coingi.exchange.client;

import com.coingi.exchange.client.entities.Currency;
import com.coingi.exchange.client.entities.CurrencyPair;
import com.coingi.exchange.client.entities.OrderType;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Function;

@RunWith(DataProviderRunner.class)
public class PublicClientForPrivateMethodsInvocationTest {

	@DataProvider
	public static Object[][] providePrivateMethods() {
		return new Object[][]{
				{
						(Function<Coingi, Object>) coingi -> coingi.getUserBalance(Currency.BTC, Currency.LTC)
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.addOrder(CurrencyPair.LTC_BTC, OrderType.ASK, BigDecimal.valueOf(1234, 2), BigDecimal.valueOf(5678, 2))
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.cancelOrder(UUID.fromString("11e7ab3e-a61e-9f14-9ca9-02429735e3c2"))
				},
				{
						(Function<Coingi, Object>) coingi -> coingi.getOrders(1, 1)
				}
		};
	}

	@Test(expected = ClientException.class)
	@UseDataProvider("providePrivateMethods")
	public void testPrivateMethodsOnPublicClient(Function<Coingi, ?> callback) {
		callback.apply(new DummyClient());
	}

}
