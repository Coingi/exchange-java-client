package com.coingi.exchange.client.entities;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * User balance for a particular currency.
 */
public class UserBalance {

	private CurrencyObject currency;

	private BigDecimal available;

	private BigDecimal inOrders;

	private BigDecimal deposited;

	private BigDecimal withdrawing;

	private BigDecimal blocked;

	public UserBalance(CurrencyObject currency, BigDecimal available, BigDecimal inOrders, BigDecimal deposited, BigDecimal withdrawing, BigDecimal blocked) {
		this.currency = currency;
		this.available = available;
		this.inOrders = inOrders;
		this.deposited = deposited;
		this.withdrawing = withdrawing;
		this.blocked = blocked;
	}

	UserBalance() {
	}

	public Currency getCurrency() {
		return Currency.getByName(currency.getName());
	}

	public BigDecimal getAvailable() {
		return available;
	}

	public BigDecimal getInOrders() {
		return inOrders;
	}

	public BigDecimal getDeposited() {
		return deposited;
	}

	public BigDecimal getWithdrawing() {
		return withdrawing;
	}

	public BigDecimal getBlocked() {
		return blocked;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserBalance that = (UserBalance) o;
		return Objects.equals(currency, that.currency) &&
				Objects.equals(available, that.available) &&
				Objects.equals(inOrders, that.inOrders) &&
				Objects.equals(deposited, that.deposited) &&
				Objects.equals(withdrawing, that.withdrawing) &&
				Objects.equals(blocked, that.blocked);
	}

	@Override
	public int hashCode() {
		return Objects.hash(currency, available, inOrders, deposited, withdrawing, blocked);
	}

}
