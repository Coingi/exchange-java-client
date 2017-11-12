package com.coingi.exchange.client.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Result container.
 */
public class Result<T> {

	private final int code;

	private final Error[] errors;

	private final T response;

	private Result(int code, Error[] errors, T response) {
		this.code = code;
		this.errors = errors;
		this.response = response;
	}

	public int getCode() {
		return code;
	}

	public boolean isSuccess() {
		return errors.length == 0;
	}

	public Collection<Error> getErrors() {
		return Collections.unmodifiableCollection(Arrays.asList(errors));
	}

	public T getResponse() {
		return response;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Result<?> result = (Result<?>) o;
		return code == result.code &&
				Arrays.equals(errors, result.errors) &&
				Objects.equals(response, result.response);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, errors, response);
	}

	public static <T> Result<T> createSuccess(T result) {
		return new Result<T>(200, new Error[0], Objects.requireNonNull(result));
	}

	public static <T> Result<T> createFailure(int code, Error[] errors) {
		return new Result<T>(code, Objects.requireNonNull(errors), null);
	}

}
