package com.coingi.exchange.client.security;

/**
 * Request nonce provider.
 */
public interface NonceProvider {

	/**
	 * Returns a new nonce.
	 *
	 * @return A new nonce
	 */
	long get();

}
