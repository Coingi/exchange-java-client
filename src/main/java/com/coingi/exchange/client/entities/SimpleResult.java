package com.coingi.exchange.client.entities;

import java.util.Objects;

abstract class SimpleResult<T> {

	private T result;

	SimpleResult(final T result) {
		this.result = result;
	}

	SimpleResult() {
	}

	public T getResult() {
		return result;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SimpleResult<?> that = (SimpleResult<?>) o;
		return Objects.equals(result, that.result);
	}

	@Override
	public int hashCode() {
		return Objects.hash(result);
	}

}
