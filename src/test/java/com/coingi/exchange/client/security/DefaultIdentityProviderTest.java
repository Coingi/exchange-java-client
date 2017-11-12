package com.coingi.exchange.client.security;

import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DefaultIdentityProviderTest {

	@Test(expected = NullPointerException.class)
	public void testInvalidCreation1() {
		new DefaultIdentityProvider(null, null);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidCreation2() {
		new DefaultIdentityProvider(new byte[]{'A'}, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCreation3() {
		new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{});
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidCreation4() {
		new DefaultIdentityProvider(null, new byte[]{'B'});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidCreation5() {
		new DefaultIdentityProvider(new byte[]{}, new byte[]{'B'});
	}

	@Test
	public void testGetPublicKey() {
		assertArrayEquals(new byte[]{'A', 'B', 'C', 'D'}, new DefaultIdentityProvider(new byte[]{'A', 'B', 'C', 'D'}, new byte[]{'E', 'F', 'G', 'H'}).getPublicKey());
	}

	@Test
	public void testSignaguee() {
		assertEquals("d2ff6070ed6560b30bf25c26c6b24f6cb47edacb71a2dc79b820c2022a5832c1", new DefaultIdentityProvider("23YVhVoBAACeapaNMMuJr484SPds9ZXfdalIrvWRb73drd5UJLf5LwYEGJcuEOWP".getBytes(Charset.forName("US-ASCII")), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes(Charset.forName("US-ASCII"))).getSignature(4284436695052033752L));
	}

}
