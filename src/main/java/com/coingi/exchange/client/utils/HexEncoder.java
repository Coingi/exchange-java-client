package com.coingi.exchange.client.utils;

/**
 * Binary to hexa convertor.
 */
public class HexEncoder {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Converts given binary data to a hexadecimal string representation.
	 *
	 * @param data Binary data
	 * @return Hexadecimal representation
	 */
	public static String encode(byte[] data) {
		final char[] hexChars = new char[data.length * 2];

		int position = 0;
		for (byte bt : data) {
			hexChars[position++] = hexArray[(bt & 0xF0) >>> 4];
			hexChars[position++] = hexArray[bt & 0x0F];
		}

		return String.valueOf(hexChars);
	}

}
