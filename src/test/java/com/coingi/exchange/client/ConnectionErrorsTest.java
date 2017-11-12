package com.coingi.exchange.client;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.io.IOException;

import static com.coingi.exchange.client.ServerRunner.runOnServer;
import static org.junit.Assert.fail;

public class ConnectionErrorsTest {

	@Test(expected = ConnectionException.class)
	public void testInvalidHost() {
		new Coingi().setHostname("invalid").getRates();
	}

	@Test(expected = ClientException.class)
	public void testUnexpectedResponse() {
		new Coingi() {

			@Override
			protected Response callApi(final HttpRequestBase request) throws IOException {
				return new Coingi.Response(401, "");
			}

		}.getRates();
	}

	@Test(expected = ClientException.class)
	public void testUnexpectedResponseNonJson() {
		new Coingi() {

			@Override
			protected Response callApi(final HttpRequestBase request) throws IOException {
				return new Coingi.Response(403, "Whatever just not valid JSON.");
			}

		}.getRates();
	}

	@Test(expected = ConnectionException.class)
	public void testTimeout() throws IOException {
		runOnServer((request, response, context) -> {
			try {
				Thread.sleep(3000);

				response.setStatusCode(200);
				response.setEntity(new StringEntity(ResponsesProvider.loadResponse("rates-ok")));
			} catch (InterruptedException e) {
				fail("Interrupted.");
			}
		}, coingi -> {
			coingi.setTimeout(10);
			coingi.getRates();
		});
	}

}
