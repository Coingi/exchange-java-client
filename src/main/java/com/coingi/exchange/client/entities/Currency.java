package com.coingi.exchange.client.entities;

import java.util.Locale;

/**
 * Supported currencies enumeration.
 */
public enum Currency {

	BTC("btc", true),
	LTC("ltc", true),
	PPC("ppc", true),
	DOGE("doge", true),
	VTC("vtc", true),
	FTC("ftc", true),
	NMC("nmc", true),
	DASH("dash", true);

	public final String identifier;

	public final boolean crypto;

	Currency(String identifier, boolean crypto) {
		this.identifier = identifier;
		this.crypto = crypto;
	}

	@Override
	public String toString() {
		return identifier;
	}

	public CurrencyObject getCurrencyObject() {
		return new CurrencyObject(this);
	}

	public static Currency getByName(String value) {
		switch (value.toLowerCase(Locale.ROOT)) {
			case "btc":
				return BTC;
			case "ltc":
				return LTC;
			case "ppc":
				return PPC;
			case "doge":
				return DOGE;
			case "vtc":
				return VTC;
			case "ftc":
				return FTC;
			case "nmc":
				return NMC;
			case "dash":
				return DASH;
			default:
				throw new IllegalArgumentException(String.format("Invalid currency type '%s'.", value));
		}
	}

}
