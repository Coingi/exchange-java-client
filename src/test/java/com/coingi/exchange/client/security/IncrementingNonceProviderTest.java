package com.coingi.exchange.client.security;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IncrementingNonceProviderTest {

	@Test
	public void testDefault() {
		final NonceProvider provider = new IncrementingNonceProvider();

		for (int expectedNonce = 1; expectedNonce < 10; ++expectedNonce) {
			assertEquals(expectedNonce, provider.get());
		}
	}

	@Test
	public void testExplicitDefault() {
		final int initialValue = 12345;
		final NonceProvider provider = new IncrementingNonceProvider(12345);

		for (int expectedNonce = initialValue; expectedNonce < initialValue + 10; ++expectedNonce) {
			assertEquals(expectedNonce, provider.get());
		}
	}

}
