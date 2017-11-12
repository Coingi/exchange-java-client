package com.coingi.exchange.client;

import com.coingi.exchange.client.security.DefaultIdentityProvider;
import com.coingi.exchange.client.security.IncrementingNonceProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class ResponsesProcessingTest extends BaseCommunicationTest {


	@Test
	@UseDataProvider("providePublicMethodsData")
	public <T> void testPublicMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, T expectedResult) {
		assertEquals(expectedResult, invocationCallback.apply(new Coingi() {
			@Override
			protected Response callApi(final HttpRequestBase request) throws IOException {
				assertEquals(expectedCallUrl, request.getURI().toString());
				assertThat(request, instanceOf(HttpGet.class));

				return ResponsesProvider.loadResponse(200, responseName);
			}
		}));
	}

	@Test
	@UseDataProvider("providePrivateReadOnlyMethodsData")
	public <T> void testPrivateReadOnlyMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, Map<String, String> expectedParameters, T expectedResult) {
		testPrivateMethods(expectedCallUrl, responseName, invocationCallback, expectedParameters, expectedResult);
	}

	@Test
	@UseDataProvider("providePrivateMethodsData")
	public <T> void testPrivateReadWriteMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, Map<String, String> expectedParameters, T expectedResult) {
		testPrivateMethods(expectedCallUrl, responseName, invocationCallback, expectedParameters, expectedResult);
	}


	private <T> void testPrivateMethods(String expectedCallUrl, String responseName, Function<Coingi, T> invocationCallback, Map<String, String> expectedParameters, T expectedResult) {
		assertEquals(expectedResult, invocationCallback.apply(new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {
			@Override
			protected Response callApi(final HttpRequestBase request) throws IOException {
				assertEquals(expectedCallUrl, request.getURI().toString());
				assertThat(request, instanceOf(HttpPost.class));

				try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
					((HttpPost) request).getEntity().writeTo(output);

					final String requestBody = new String(output.toByteArray(), "UTF-8");
					final Map<String, String> parameters = (new Gson()).fromJson(requestBody, new TypeToken<Map<String, String>>() {

					}.getType());

					assertNotNull(parameters.get("nonce"));
					parameters.remove("nonce");

					assertNotNull(parameters.get("token"));
					parameters.remove("token");

					assertNotNull(parameters.get("signature"));
					parameters.remove("signature");

					assertEquals(expectedParameters, parameters);
				}

				return ResponsesProvider.loadResponse(200, responseName);
			}
		}));
	}


	@Test(expected = ApiCallException.class)
	@UseDataProvider("provideMethodErrorsData")
	public void testMethodErrors(String responseName, Function<Coingi, ?> invocationCallback, ApiCallException expectedException) {
		try {
			invocationCallback.apply(new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {
				@Override
				protected Response callApi(final HttpRequestBase request) throws IOException {
					return ResponsesProvider.loadResponse(400, responseName);
				}
			});
		} catch (ApiCallException ex) {
			assertFunctionExceptionEquals(expectedException, ex);

			throw ex;
		}
	}

}
