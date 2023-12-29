package com.clebsonsantos.backend.entity;

import java.math.BigDecimal;
import java.util.List;

public record TransactionReport(
		String storeName,
		BigDecimal total,
		List<Transactions> transactions) {

	public TransactionReport addTotal(BigDecimal value) {
		return new TransactionReport(storeName, total.add(value), transactions);
	}

	public TransactionReport addTransaction(Transactions transaction) {
		transactions.add(transaction);
		return new TransactionReport(storeName, total, transactions);
	}
}
