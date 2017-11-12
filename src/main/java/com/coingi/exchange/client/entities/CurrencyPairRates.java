package com.coingi.exchange.client.entities;

import java.util.Collection;

/**
 * List of currency pair rates.
 */
public class CurrencyPairRates extends ResultList<CurrencyPairRate> {

	public CurrencyPairRates(final Collection<CurrencyPairRate> c) {
		super(c);
	}

	CurrencyPairRates() {
	}

}
