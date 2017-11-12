package com.coingi.exchange.client.security;

import com.coingi.exchange.client.utils.HexEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;

/**
 * A default thread-safe identity provider implementation.
 */
public class DefaultIdentityProvider implements IdentityProvider {

	private final static String ALGO = "HmacSHA256";

	private final static Charset CHARSET = Charset.forName("US-ASCII");

	private final byte[] publicKey;

	private final Mac sha256HMAC;

	/**
	 * Initializes the provider using the given public and private API key.
	 *
	 * @param publicKey  Public API key part
	 * @param privateKey Private API key part
	 */
	public DefaultIdentityProvider(byte[] publicKey, byte[] privateKey) {
		Objects.requireNonNull(publicKey, "No public key given.");
		if (publicKey.length == 0) {
			throw new IllegalArgumentException("Empty public key given.");
		}

		this.publicKey = publicKey;

		try {
			Objects.requireNonNull(privateKey, "No private key given.");
			if (privateKey.length == 0) {
				throw new IllegalArgumentException("Empty private key given.");
			}

			this.sha256HMAC = Mac.getInstance(ALGO);
			this.sha256HMAC.init(new SecretKeySpec(privateKey, ALGO));
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		} catch (InvalidKeyException ex) {
			throw new IllegalArgumentException("Invalid private key.", ex);
		}
	}

	/**
	 * Returns the public key part.
	 *
	 * @return Public key
	 */
	@Override
	public byte[] getPublicKey() {
		return publicKey;
	}

	/**
	 * Returns a valid signature for the given nonce.
	 *
	 * @param nonce Request nonce
	 * @return Request signature
	 */
	@Override
	synchronized public String getSignature(long nonce) {
		return HexEncoder.encode(sha256HMAC.doFinal(buildSignatureData(nonce))).toLowerCase(Locale.ROOT);
	}

	byte[] buildSignatureData(long nonce) {
		final byte[] nonceData = String.valueOf(nonce).getBytes(CHARSET);
		final byte[] data = new byte[nonceData.length + 1 + publicKey.length];

		System.arraycopy(nonceData, 0, data, 0, nonceData.length);
		data[nonceData.length] = '$';
		System.arraycopy(publicKey, 0, data, nonceData.length + 1, publicKey.length);

		return data;
	}

}
