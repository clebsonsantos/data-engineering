package com.clebsonsantos.backend.entity;

import java.math.BigDecimal;

public record TransactionCNAB(
		Integer type,
		String createdAt,
		BigDecimal amount,
		Long cpf,
		String card,
		String dateHour,
		String storeOwner,
		String storeName) {

}