package com.coingi.exchange.client.entities;

/**
 * Order status enumeration.
 */
public enum OrderStatus {

	ACTIVE((short) 0), PARTIALLY_PROCESSED((short) 1), PROCESSED((short) 2), CANCELLED((short) 3), BLOCKED((short) 4);

	public final short value;

	OrderStatus(final short value) {
		this.value = value;
	}

	public static OrderStatus getByIdentifier(short type) {
		switch (type) {
			case 0:
				return ACTIVE;
			case 1:
				return PARTIALLY_PROCESSED;
			case 2:
				return PROCESSED;
			case 3:
				return CANCELLED;
			case 4:
				return BLOCKED;
			default:
				throw new IllegalArgumentException("Invalid order status.");
		}
	}

}
