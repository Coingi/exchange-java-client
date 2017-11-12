package com.coingi.exchange.client.entities;

import java.util.Collection;

/**
 * List of currency pair rolling aggregations.
 */
public class RollingAggregationsList extends ResultList<RollingAggregation> {

	RollingAggregationsList() {
	}

	public RollingAggregationsList(Collection<RollingAggregation> c) {
		super(c);
	}

}
