package com.clebsonsantos.backend.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.clebsonsantos.backend.entity.TransactionReport;
import com.clebsonsantos.backend.service.TransactionService;

@RestController
@RequestMapping("transactions")
public class TransactionController {
  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping()
  public List<TransactionReport> listAll() {
    return this.transactionService.getTotalsTransactionsByStoreName();
  }
}
