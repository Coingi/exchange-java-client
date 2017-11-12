package com.coingi.exchange.client.entities;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Currency pair rate.
 */
public class CurrencyPairRate {

	private CurrencyPair currencyPair;

	private BigDecimal rate;

	public CurrencyPairRate(CurrencyPair currencyPair, BigDecimal rate) {
		this.currencyPair = currencyPair;
		this.rate = rate;
	}

	CurrencyPairRate() {
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}

	public BigDecimal getRate() {
		return rate;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CurrencyPairRate that = (CurrencyPairRate) o;
		return Objects.equals(currencyPair, that.currencyPair) &&
				Objects.equals(rate, that.rate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(currencyPair, rate);
	}

}
