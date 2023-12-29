package com.clebsonsantos.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clebsonsantos.backend.entity.TransactionReport;
import com.clebsonsantos.backend.repository.TransactionRepository;

@Service
public class TransactionService {
  private final TransactionRepository repository;

  public TransactionService(TransactionRepository repository) {
    this.repository = repository;
  }

  public List<TransactionReport> getTotalsTransactionsByStoreName() {
    var transactions = this.repository.findAllByOrderByStoreNameAscIdDesc();
    var reportMap = new LinkedHashMap<String, TransactionReport>();

    transactions.forEach(transaction -> {
      var storeName = transaction.storeName();
      var amount = transaction.amount();

      reportMap.compute(storeName, (key, existing) -> {
        var report = (existing != null) ? existing : new TransactionReport(key, BigDecimal.ZERO, new ArrayList<>());
        return report.addTotal(amount).addTransaction(transaction);
      });
    });

    return new ArrayList<>(reportMap.values());
  }
}
