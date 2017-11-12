package com.coingi.exchange.client;

import com.coingi.exchange.client.entities.Error;

import java.util.Collection;
import java.util.Collections;
import java.util.StringJoiner;

/**
 * Response exception - the API returned a non-200 response.
 */
public class ApiCallException extends ClientException {

	private final int statusCode;

	private final Collection<Error> errors;

	public ApiCallException(int statusCode) {
		this(statusCode, Collections.emptyList());
	}

	public ApiCallException(int statusCode, Collection<Error> errors) {
		super("There was a problem performing the request.");

		this.statusCode = statusCode;
		this.errors = Collections.unmodifiableCollection(errors);
	}


	public int getStatusCode() {
		return statusCode;
	}

	public Collection<Error> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		final StringJoiner joiner = new StringJoiner(",", "ApiCallException{", "}");
		for (final Error error : errors) {
			joiner.add(String.format("%d:%s", error.getCode(), error.getMessage()));
		}

		return joiner.toString();
	}

}
