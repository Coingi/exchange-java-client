package com.coingi.exchange.client.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTypeTest {

	@Test
	public void testStaticGetterConsistency() {
		for (final OrderType orderType : OrderType.values()) {
			assertEquals(orderType, OrderType.getByIdentifier(orderType.type));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalid() {
		OrderType.getByIdentifier((short) 2);
	}

}
