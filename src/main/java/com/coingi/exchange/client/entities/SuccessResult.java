package com.coingi.exchange.client.entities;

/**
 * Empty result container.
 */
public class SuccessResult extends SimpleResult<Boolean> {

	public boolean isSuccess() {
		return getResult();
	}

}
