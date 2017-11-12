package com.coingi.exchange.client.entities;

import java.util.Collection;

/**
 * List of user balances.
 */
public class UserBalancesList extends ResultList<UserBalance> {

	UserBalancesList() {
	}

	public UserBalancesList(Collection<UserBalance> c) {
		super(c);
	}

}
