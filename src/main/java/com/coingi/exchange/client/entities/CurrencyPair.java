package com.coingi.exchange.client.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Currency pair.
 */
public class CurrencyPair {

	public final static CurrencyPair LTC_BTC = new CurrencyPair(Currency.LTC, Currency.BTC);

	public final static CurrencyPair PPC_BTC = new CurrencyPair(Currency.PPC, Currency.BTC);

	public final static CurrencyPair DOGE_BTC = new CurrencyPair(Currency.DOGE, Currency.BTC);

	public final static CurrencyPair VTC_BTC = new CurrencyPair(Currency.VTC, Currency.BTC);

	public final static CurrencyPair NMC_BTC = new CurrencyPair(Currency.NMC, Currency.BTC);

	public final static CurrencyPair DASH_BTC = new CurrencyPair(Currency.DASH, Currency.BTC);

	public final static CurrencyPair BTC_USD = new CurrencyPair(Currency.BTC, Currency.USD);

	public final static CurrencyPair BTC_EUR = new CurrencyPair(Currency.BTC, Currency.EUR);

	public final static CurrencyPair LTC_USD = new CurrencyPair(Currency.LTC, Currency.USD);

	public final static CurrencyPair LTC_EUR = new CurrencyPair(Currency.LTC, Currency.EUR);

	public final static CurrencyPair PPC_USD = new CurrencyPair(Currency.PPC, Currency.USD);

	public final static CurrencyPair PPC_EUR = new CurrencyPair(Currency.PPC, Currency.EUR);

	public final static CurrencyPair DOGE_USD = new CurrencyPair(Currency.DOGE, Currency.USD);

	public final static CurrencyPair EUR_USD = new CurrencyPair(Currency.EUR, Currency.USD);

	public final static Collection<CurrencyPair> SUPPORTED_CURRENCY_PAIRS = Collections.unmodifiableList(Arrays.asList(BTC_USD, BTC_EUR, LTC_USD, LTC_EUR, PPC_USD, PPC_EUR, DOGE_USD, EUR_USD, LTC_BTC, PPC_BTC, DOGE_BTC, VTC_BTC, NMC_BTC, DASH_BTC));

	private String base;

	private String counter;

	private CurrencyPair(Currency base, Currency counter) {
		this.base = Objects.requireNonNull(base.identifier);
		this.counter = Objects.requireNonNull(counter.identifier);
	}

	CurrencyPair() {
	}

	public Currency getBaseCurrency() {
		return Currency.getByName(base);
	}

	public Currency getCounterCurrency() {
		return Currency.getByName(counter);
	}

	@Override
	public String toString() {
		return base + "-" + counter;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CurrencyPair that = (CurrencyPair) o;
		return Objects.equals(base, that.base) && Objects.equals(counter, that.counter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(base, counter);
	}

}
