package com.coingi.exchange.client;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import static com.coingi.exchange.client.ServerRunner.runOnServer;
import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class DummyHttpCommunicationTest extends BaseCommunicationTest {

	@Test
	@UseDataProvider("providePublicMethodsData")
	public <T> void testPublicMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, T expectedResult) throws IOException {
		testMethod(responseName, invocationCallback, expectedResult);
	}

	@Test
	@UseDataProvider("providePrivateReadOnlyMethodsData")
	public <T> void testPrivateReadOnlyMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, Map<String, String> expectedParameters, T expectedResult) throws IOException {
		testMethod(responseName, invocationCallback, expectedResult);
	}

	@Test
	@UseDataProvider("providePrivateMethodsData")
	public <T> void testPrivateReadWriteMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, Map<String, String> expectedParameters, T expectedResult) throws IOException {
		testMethod(responseName, invocationCallback, expectedResult);
	}


	private <T> void testMethod(String responseName, Function<Coingi, T> invocationCallback, T expectedResult) throws IOException {
		runOnServer(200, responseName, coingi -> assertEquals(expectedResult, invocationCallback.apply(coingi)));
	}

	@Test(expected = ApiCallException.class)
	@UseDataProvider("provideMethodErrorsData")
	public void testMethodErrors(String responseName, Function<Coingi, ?> invocationCallback, ApiCallException expectedException) throws IOException {
		runOnServer(400, responseName, coingi -> {
			try {
				invocationCallback.apply(coingi);
			} catch (ApiCallException ex) {
				assertFunctionExceptionEquals(expectedException, ex);

				throw ex;
			}
		});
	}

}
