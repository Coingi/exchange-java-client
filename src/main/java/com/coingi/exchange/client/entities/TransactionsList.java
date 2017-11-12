package com.coingi.exchange.client.entities;

import java.util.Collection;

/**
 * Transactions list.
 */
public class TransactionsList extends ResultList<Transaction> {

	TransactionsList() {
	}

	public TransactionsList(Collection<Transaction> c) {
		super(c);
	}

}
