package com.clebsonsantos.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.clebsonsantos.backend.entity.Transactions;

public interface TransactionRepository extends CrudRepository<Transactions, Long> {
  List<Transactions> findAllByOrderByStoreNameAscIdDesc();
}
