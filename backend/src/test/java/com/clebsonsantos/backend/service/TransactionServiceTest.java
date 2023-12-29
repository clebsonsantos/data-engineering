package com.clebsonsantos.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.clebsonsantos.backend.entity.Transactions;
import com.clebsonsantos.backend.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
  final String storeA = "Store A", storeB = "Store B";

  @InjectMocks
  private TransactionService transactionService;

  @Mock
  private TransactionRepository transactionRepository;

  @Test
  public void testGetTotalsTransactionsByStoreName() {

    var transactionA = new Transactions(1L, 1, new Date(System.currentTimeMillis()), BigDecimal.valueOf(100),
        123456789L,
        "1234-5678-9012-3456", new Time(System.currentTimeMillis()), "owner store A", this.storeA);

    var transactionB = new Transactions(2L, 1, new Date(System.currentTimeMillis()), BigDecimal.valueOf(500),
        123457789L,
        "1234-5678-9012-0000", new Time(System.currentTimeMillis()), "owner store C", this.storeB);

    var transactionC = new Transactions(3L, 1, new Date(System.currentTimeMillis()), BigDecimal.valueOf(70),
        333456789L,
        "1234-5678-9012-1111", new Time(System.currentTimeMillis()), "owner store A", this.storeA);

    var mockTransactions = List.of(transactionA, transactionB, transactionC);

    when(this.transactionRepository.findAllByOrderByStoreNameAscIdDesc()).thenReturn(mockTransactions);

    var reports = this.transactionService.getTotalsTransactionsByStoreName();

    assertEquals(2, reports.size());

    reports.forEach(report -> {
      if (report.storeName().equals(this.storeA)) {
        assertEquals(2, report.transactions().size());
        assertEquals(BigDecimal.valueOf(170), report.total());

        assertTrue(report.transactions().contains(transactionA));
        assertTrue(report.transactions().contains(transactionC));

      } else if (report.storeName().equals(this.storeB)) {

        assertEquals(1, report.transactions().size());
        assertEquals(BigDecimal.valueOf(500), report.total());

        assertTrue(report.transactions().contains(transactionB));
      }
    });
  }
}
