package com.coingi.exchange.client;

import com.coingi.exchange.client.security.DefaultIdentityProvider;
import com.coingi.exchange.client.security.IncrementingNonceProvider;
import org.junit.Test;

public class ClientInvalidCreationTest {

	@Test(expected = NullPointerException.class)
	public void testInvalidCreation1() {
		new Coingi(new IncrementingNonceProvider(), null);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidCreation2() {
		new Coingi(null, new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'}));
	}

}
