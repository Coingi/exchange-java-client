package com.coingi.exchange.client;

import com.coingi.exchange.client.security.DefaultIdentityProvider;
import com.coingi.exchange.client.security.IncrementingNonceProvider;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

class ServerRunner {

	static void runOnServer(int httpResponseCode, String responseFileName, Consumer<Coingi> callback) throws IOException {
		runOnServer((request, response, context) -> {
			response.setStatusCode(httpResponseCode);
			response.setEntity(new StringEntity(ResponsesProvider.loadResponse(responseFileName)));
		}, callback);
	}

	static void runOnServer(HttpRequestHandler handler, Consumer<Coingi> callback) throws IOException {
		final SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(15000).build();
		final ServerBootstrap serverBootstrap = ServerBootstrap.bootstrap().setSocketConfig(socketConfig).setServerInfo("TEST/1.1").registerHandler("/*", handler);

		final HttpServer httpServer = serverBootstrap.create();
		httpServer.start();

		try {
			callback.accept(new Coingi(new IncrementingNonceProvider(), new DefaultIdentityProvider(new byte[]{'A'}, new byte[]{'B'})) {

				@Override
				String createEntryPoint(String hostname) {
					return super.createEntryPoint(hostname).replace("https://", "http://");
				}

			}.setHostname(String.format("localhost:%d", httpServer.getLocalPort())));
		} finally {
			httpServer.shutdown(0, TimeUnit.SECONDS);
		}
	}

}
