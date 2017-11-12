package com.coingi.exchange.client.entities;

import java.util.Objects;

/**
 * Error definition.
 */
public class Error {

	private int code;

	private String message;

	public Error(int code, String message) {
		this.code = code;
		this.message = Objects.requireNonNull(message);
	}

	Error() {
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Error error = (Error) o;
		return code == error.code &&
				Objects.equals(message, error.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, message);
	}

}
