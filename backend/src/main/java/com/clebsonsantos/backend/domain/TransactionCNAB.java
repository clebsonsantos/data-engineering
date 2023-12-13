package com.clebsonsantos.backend.domain;

import java.math.BigDecimal;

public record TransactionCNAB(
        Integer type,
        String date,
        BigDecimal amount,
        Long cpf,
        String card,
        String hour,
        String storeOwner,
        String storeName) {

}