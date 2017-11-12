package com.coingi.exchange.client.security;

/**
 * A thread-safe incrementing nonce provider.
 */
public class IncrementingNonceProvider implements NonceProvider {

	private long currentNonce;

	/**
	 * Creates the provider.
	 * <p>
	 * The first returned nonce will be 1.
	 */
	public IncrementingNonceProvider() {
		this(1);
	}

	/**
	 * Creates the provider with the given initial nonce.
	 * <p>
	 * The first returned nonce will be the initial one.
	 *
	 * @param initialNonce Initial nonce value.
	 */
	public IncrementingNonceProvider(long initialNonce) {
		this.currentNonce = initialNonce;
	}

	public synchronized long get() {
		return currentNonce++;
	}

}
