package com.coingi.exchange.client.entities;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Depth range.
 */
public class DepthRange {

	private BigDecimal price;

	private BigDecimal amount;

	public DepthRange(BigDecimal price, BigDecimal amount) {
		this.price = Objects.requireNonNull(price);
		this.amount = Objects.requireNonNull(amount);
	}

	DepthRange() {
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DepthRange that = (DepthRange) o;
		return Objects.equals(price, that.price) &&
				Objects.equals(amount, that.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(price, amount);
	}

}
