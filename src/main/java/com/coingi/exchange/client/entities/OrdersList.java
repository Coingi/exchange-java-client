package com.coingi.exchange.client.entities;

import java.util.List;

/**
 * Orders list.
 */
public class OrdersList extends PaginatedResultList<Order> {

	private List<Order> orders;

	public OrdersList(boolean hasMore, List<Order> orders) {
		super(hasMore);

		this.orders = orders;
	}

	public OrdersList() {
	}

	@Override
	protected List<Order> getResultsList() {
		return orders;
	}

}
