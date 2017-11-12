package com.coingi.exchange.client;

public class ClientException extends RuntimeException {

	ClientException(String message) {
		super(message);
	}

	ClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
