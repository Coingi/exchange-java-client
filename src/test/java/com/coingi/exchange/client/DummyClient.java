package com.coingi.exchange.client;

import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;

class DummyClient extends Coingi {

	@Override
	protected Response callApi(final HttpRequestBase request) throws IOException {
		return ResponsesProvider.loadResponse(500, "internal-error");
	}

}
