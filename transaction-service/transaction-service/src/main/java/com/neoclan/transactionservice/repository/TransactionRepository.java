package com.neoclan.transactionservice.repository;

import com.neoclan.transactionservice.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
   List<TransactionEntity> findByAccountNumber(String accountNumber);
}
