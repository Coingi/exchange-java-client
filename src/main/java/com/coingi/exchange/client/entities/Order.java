package com.coingi.exchange.client.entities;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * User order.
 */
public class Order {

	private UUID id;

	private short type;

	private long timestamp;

	private CurrencyPair currencyPair;

	private BigDecimal price;

	private BigDecimal baseAmount;

	private BigDecimal counterAmount;

	private BigDecimal originalBaseAmount;

	private BigDecimal originalCounterAmount;

	private short status;

	public Order(UUID id, OrderType type, long timestamp, CurrencyPair currencyPair, BigDecimal price, BigDecimal baseAmount, BigDecimal counterAmount, BigDecimal originalBaseAmount, BigDecimal originalCounterAmount, OrderStatus status) {
		this.id = id;
		this.type = type.type;
		this.timestamp = timestamp;
		this.currencyPair = currencyPair;
		this.price = price;
		this.baseAmount = baseAmount;
		this.counterAmount = counterAmount;
		this.originalBaseAmount = originalBaseAmount;
		this.originalCounterAmount = originalCounterAmount;
		this.status = status.value;
	}

	Order() {
	}

	public UUID getId() {
		return id;
	}

	public OrderType getType() {
		return OrderType.getByIdentifier(type);
	}

	public long getTimestamp() {
		return timestamp;
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

	public BigDecimal getOriginalBaseAmount() {
		return originalBaseAmount;
	}

	public BigDecimal getOriginalCounterAmount() {
		return originalCounterAmount;
	}

	public OrderStatus getStatus() {
		return OrderStatus.getByIdentifier(status);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return type == order.type &&
				timestamp == order.timestamp &&
				status == order.status &&
				Objects.equals(id, order.id) &&
				Objects.equals(currencyPair, order.currencyPair) &&
				Objects.equals(price, order.price) &&
				Objects.equals(baseAmount, order.baseAmount) &&
				Objects.equals(counterAmount, order.counterAmount) &&
				Objects.equals(originalBaseAmount, order.originalBaseAmount) &&
				Objects.equals(originalCounterAmount, order.originalCounterAmount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type, timestamp, currencyPair, price, baseAmount, counterAmount, originalBaseAmount, originalCounterAmount, status);
	}

}
