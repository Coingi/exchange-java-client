package com.coingi.exchange.client.utils;

import java.util.StringJoiner;

/**
 * Helper class for building URLs.
 */
public class UrlBuilder {


	/**
	 * Creates an URL path from given parts.
	 *
	 * @param parts URL path parts
	 * @return URL path
	 * @throws IllegalArgumentException If a part is NULL
	 */
	public static String buildUrlPath(Object... parts) {
		if (parts == null || parts.length == 0) {
			return "";
		}

		final StringJoiner stringJoiner = new StringJoiner("/");

		for (final Object part : parts) {
			if (part == null) {
				throw new IllegalArgumentException("Path parts cannot be null.");
			}

			stringJoiner.add(String.valueOf(part));
		}

		return stringJoiner.toString();
	}

}
