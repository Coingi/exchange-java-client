package com.coingi.exchange.client.entities;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

abstract class PaginatedResultList<T> implements Iterable<T> {

	private boolean hasMore;

	public PaginatedResultList(boolean hasMore) {
		this.hasMore = hasMore;
	}

	PaginatedResultList() {
	}

	public final boolean hasMore() {
		return hasMore;
	}

	abstract protected List<T> getResultsList();

	public final List<T> getList() {
		return Collections.unmodifiableList(getResultsList());
	}

	@Override
	public Iterator<T> iterator() {
		return getList().iterator();
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PaginatedResultList<?> that = (PaginatedResultList<?>) o;
		return hasMore == that.hasMore && Objects.equals(getResultsList(), that.getResultsList());
	}

	@Override
	public final int hashCode() {
		return Objects.hash(hasMore, getResultsList());
	}

}
