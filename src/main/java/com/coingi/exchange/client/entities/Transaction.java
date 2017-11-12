package com.coingi.exchange.client.entities;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A single transaction.
 */
public class Transaction {

	private String id;

	private long timestamp;

	private CurrencyPair currencyPair;

	private BigDecimal amount;

	private BigDecimal price;

	private short type;

	public Transaction(UUID id, long timestamp, CurrencyPair currencyPair, BigDecimal amount, BigDecimal price, OrderType type) {
		this.id = id.toString();
		this.timestamp = timestamp;
		this.currencyPair = Objects.requireNonNull(currencyPair);
		this.amount = Objects.requireNonNull(amount);
		this.price = Objects.requireNonNull(price);
		this.type = type.type;
	}

	Transaction() {
	}

	public UUID getId() {
		return UUID.fromString(id);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public OrderType getType() {
		return OrderType.getByIdentifier(type);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Transaction that = (Transaction) o;
		return timestamp == that.timestamp &&
				type == that.type &&
				Objects.equals(id, that.id) &&
				Objects.equals(currencyPair, that.currencyPair) &&
				Objects.equals(amount, that.amount) &&
				Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, timestamp, currencyPair, amount, price, type);
	}

}
