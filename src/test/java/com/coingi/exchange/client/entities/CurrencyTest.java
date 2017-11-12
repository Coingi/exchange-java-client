package com.coingi.exchange.client.entities;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class CurrencyTest {

	@Test
	public void testStaticGetterConsistency() {
		for (final Currency currency : Currency.values()) {
			assertEquals(currency, Currency.getByName(currency.identifier));
		}
	}

	@Test
	public void testStaticGetterCaseInsensitive() {
		for (final Currency currency : Currency.values()) {
			assertEquals(currency, Currency.getByName(currency.identifier.toUpperCase(Locale.ROOT)));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalid() {
		Currency.getByName("nonexistent");
	}

}
