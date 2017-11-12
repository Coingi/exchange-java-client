package com.coingi.exchange.client.security;

/**
 * Logged user identity provider.
 */
public interface IdentityProvider {

	/**
	 * Returns the public key part.
	 *
	 * @return Public key
	 */
	byte[] getPublicKey();

	/**
	 * Returns a valid signature for the given nonce.
	 *
	 * @param nonce Request nonce
	 * @return Request signature
	 */
	String getSignature(long nonce);

}
