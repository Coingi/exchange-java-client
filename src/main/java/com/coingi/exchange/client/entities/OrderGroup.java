package com.coingi.exchange.client.entities;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Orders grouped by value.
 */
public class OrderGroup {

	private short type;

	private CurrencyPair currencyPair;

	private BigDecimal price;

	private BigDecimal baseAmount;

	private BigDecimal counterAmount;

	public OrderGroup(OrderType type, CurrencyPair currencyPair, BigDecimal price, BigDecimal baseAmount, BigDecimal counterAmount) {
		this.type = type.type;
		this.currencyPair = Objects.requireNonNull(currencyPair);
		this.price = Objects.requireNonNull(price);
		this.baseAmount = Objects.requireNonNull(baseAmount);
		this.counterAmount = Objects.requireNonNull(counterAmount);
	}

	OrderGroup() {
	}

	public OrderType getType() {
		return OrderType.getByIdentifier(type);
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getBaseAmount() {
		return baseAmount;
	}

	public BigDecimal getCounterAmount() {
		return counterAmount;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderGroup that = (OrderGroup) o;
		return type == that.type &&
				Objects.equals(currencyPair, that.currencyPair) &&
				Objects.equals(price, that.price) &&
				Objects.equals(baseAmount, that.baseAmount) &&
				Objects.equals(counterAmount, that.counterAmount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, currencyPair, price, baseAmount, counterAmount);
	}

}
