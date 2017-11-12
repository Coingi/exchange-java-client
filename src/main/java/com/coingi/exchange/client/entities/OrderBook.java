package com.coingi.exchange.client.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Current order book.
 */
public class OrderBook {

	private List<OrderGroup> asks;

	private List<OrderGroup> bids;

	private List<DepthRange> askDepthRange;

	private List<DepthRange> bidDepthRange;

	public OrderBook(List<OrderGroup> asks, List<OrderGroup> bids, List<DepthRange> askDepthRange, List<DepthRange> bidDepthRange) {
		this.asks = Objects.requireNonNull(asks);
		this.bids = Objects.requireNonNull(bids);
		this.askDepthRange = Objects.requireNonNull(askDepthRange);
		this.bidDepthRange = Objects.requireNonNull(bidDepthRange);
	}

	OrderBook() {
	}

	public Collection<OrderGroup> getAsks() {
		return Collections.unmodifiableCollection(asks);
	}

	public Collection<OrderGroup> getBids() {
		return Collections.unmodifiableCollection(bids);
	}

	public Collection<DepthRange> getAskDepthRange() {
		return Collections.unmodifiableCollection(askDepthRange);
	}

	public Collection<DepthRange> getBidDepthRange() {
		return Collections.unmodifiableCollection(bidDepthRange);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderBook orderBook = (OrderBook) o;
		return Objects.equals(asks, orderBook.asks) &&
				Objects.equals(bids, orderBook.bids) &&
				Objects.equals(askDepthRange, orderBook.askDepthRange) &&
				Objects.equals(bidDepthRange, orderBook.bidDepthRange);
	}

	@Override
	public int hashCode() {
		return Objects.hash(asks, bids, askDepthRange, bidDepthRange);
	}

}
