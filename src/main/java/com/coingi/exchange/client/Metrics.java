package com.coingi.exchange.client;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Basic metrics container.
 */
public class Metrics {

	private final AtomicLong publicRequestCounter = new AtomicLong(0);
	private final AtomicLong userRequestCounter = new AtomicLong(0);
	private final AtomicLong validResponseCounter = new AtomicLong(0);
	private final AtomicLong errorResponseCounter = new AtomicLong(0);
	private final AtomicLong errorCounter = new AtomicLong(0);

	/**
	 * Registers a public request and returns the total number of public requests so far.
	 *
	 * @return Number of public requests
	 */
	long registerPublicRequest() {
		return publicRequestCounter.incrementAndGet();
	}

	/**
	 * Returns the number of public requests so far.
	 *
	 * @return Number of public requests
	 */
	public long getPublicRequestCount() {
		return publicRequestCounter.get();
	}

	/**
	 * Registers a user request and returns the total number of user requests so far.
	 *
	 * @return Number of user requests
	 */
	long registerUserRequest() {
		return userRequestCounter.incrementAndGet();
	}

	/**
	 * Returns the number of user requests so far.
	 *
	 * @return Number of user requests
	 */
	public long getUserRequestCount() {
		return userRequestCounter.get();
	}


	/**
	 * Registers a request response and returns the number of responses processed so far.
	 *
	 * @param success Was the response successful?
	 * @return Number of responses processed
	 */
	long registerResponse(boolean success) {
		return success ? validResponseCounter.incrementAndGet() : errorResponseCounter.incrementAndGet();
	}

	/**
	 * Returns the number of valid responses received.
	 *
	 * @return Number of responses received
	 */
	public long getValidResponseCount() {
		return validResponseCounter.get();
	}

	/**
	 * Returns the number of error responses received.
	 *
	 * @return Number of responses received
	 */
	public long getErrorResponseCount() {
		return errorResponseCounter.get();
	}

	/**
	 * Registers a request error and returns the number or errors so far.
	 *
	 * @return Number of errors encountered
	 */
	long registerRequestError() {
		return errorCounter.incrementAndGet();
	}

	/**
	 * Returns the number of errors encountered.
	 *
	 * @return Number of errors encountered
	 */
	public long getRequestErrorCount() {
		return errorCounter.get();
	}

}
