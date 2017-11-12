package com.coingi.exchange.client.entities;

import java.util.Objects;

/**
 * Currency object used in responses
 */
public class CurrencyObject {

	private String name;

	private boolean crypto;

	CurrencyObject() {
	}

	CurrencyObject(Currency currency) {
		this.name = currency.identifier;
		this.crypto = currency.crypto;
	}

	public String getName() {
		return name;
	}

	public boolean isCrypto() {
		return crypto;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CurrencyObject that = (CurrencyObject) o;
		return crypto == that.crypto &&
				Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, crypto);
	}

}
