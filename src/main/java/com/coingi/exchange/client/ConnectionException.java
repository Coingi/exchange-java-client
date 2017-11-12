package com.coingi.exchange.client;

/**
 * Connection exception - there was an I/O error performing the request.
 */
public class ConnectionException extends ClientException {

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
