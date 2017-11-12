package com.coingi.exchange.client.entities;

/**
 * Order type enumeration.
 */
public enum OrderType {

	BID((short) 0), ASK((short) 1);

	public final short type;

	OrderType(final short type) {
		this.type = type;
	}

	public static OrderType getByIdentifier(short type) {
		switch (type) {
			case 0:
				return BID;
			case 1:
				return ASK;
			default:
				throw new IllegalArgumentException("Invalid order type.");
		}
	}

}
