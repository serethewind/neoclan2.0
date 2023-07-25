package com.neoclan.transactionservice.repository;

import com.neoclan.transactionservice.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
