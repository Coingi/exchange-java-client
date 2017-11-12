package com.coingi.exchange.client;

import com.coingi.exchange.client.security.DefaultIdentityProvider;
import com.coingi.exchange.client.security.IncrementingNonceProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;

@RunWith(DataProviderRunner.class)
public class RealHttpCommunicationTest extends BaseCommunicationTest {

	@Test
	@UseDataProvider("providePublicMethodsData")
	public <T> void testPublicMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, T expectedResult) {
		testMethod(responseName, invocationCallback, expectedResult);
	}

	@Test
	@UseDataProvider("providePrivateReadOnlyMethodsData")
	public <T> void testPrivateReadOnlyMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, Map<String, String> expectedParameters, T expectedResult) {
		testMethod(responseName, invocationCallback, expectedResult);
	}


	private <T> void testMethod(String responseName, Function<Coingi, T> invocationCallback, T expectedResult) {
		final String hostname = System.getenv("TEST_API_HOSTNAME");
		final String publicKey = System.getenv("TEST_API_KEY_PUBLIC");
		final String privateKey = System.getenv("TEST_API_KEY_PRIVATE");

		assumeThat("TEST_API_HOSTNAME, TEST_API_KEY_PUBLIC and TEST_API_KEY_PRIVATE environment variables have to be configured.", Arrays.asList(hostname, publicKey, privateKey), everyItem(notNullValue()));
		assumeTrue("Hostname empty.", !hostname.isEmpty());
		assumeTrue("Public key empty.", !publicKey.isEmpty());
		assumeTrue("Private key empty.", !privateKey.isEmpty());

		try {
			InetAddress.getByName(hostname);
		} catch (UnknownHostException ex) {
			assumeTrue(String.format("Hostname %s not accessible.", hostname), false);
		}

		final T result = invocationCallback.apply(
				new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(publicKey.getBytes(Charset.forName("US-ASCII")), privateKey.getBytes(Charset.forName("US-ASCII")))).setHostname(hostname)
		);

		assertThat(result, instanceOf(expectedResult instanceof Collection ? Collection.class : expectedResult.getClass()));
	}

}
