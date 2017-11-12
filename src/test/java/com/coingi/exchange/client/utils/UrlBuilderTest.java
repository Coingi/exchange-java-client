package com.coingi.exchange.client.utils;

import com.coingi.exchange.client.entities.Currency;
import com.coingi.exchange.client.entities.CurrencyPair;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class UrlBuilderTest {

	@DataProvider
	public static Object[][] provideData() {
		return new Object[][]{
				{
						null,
						""
				},
				{
						new Object[]{},
						""
				},
				{
						new Object[]{"A"},
						"A"
				},
				{
						new Object[]{1},
						"1"
				},
				{
						new Object[]{BigDecimal.valueOf(1024)},
						"1024"
				},
				{
						new Object[]{BigDecimal.valueOf(1234, 2)},
						"12.34"
				},
				{
						new Object[]{Currency.LTC},
						"ltc"
				},
				{
						new Object[]{CurrencyPair.LTC_BTC},
						"ltc-btc"
				},
				{
						new Object[]{"A", 1, BigDecimal.valueOf(1024), BigDecimal.valueOf(1234, 2), Currency.LTC, CurrencyPair.LTC_BTC},
						"A/1/1024/12.34/ltc/ltc-btc"
				},
		};
	}

	@Test
	@UseDataProvider("provideData")
	public void test(Object[] parameters, String expectedPath) {
		assertEquals(expectedPath, UrlBuilder.buildUrlPath(parameters));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullParameter() {
		UrlBuilder.buildUrlPath("A", "B", null, "C");
	}


}
