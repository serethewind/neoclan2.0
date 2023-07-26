package com.neoclan.transactionservice.dto;

import com.neoclan.transactionservice.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionResponseDto {
    private String id;
    private TransactionType transactionType;
    private String accountNumber;
    private BigDecimal amount;
    private LocalDateTime timePerformed;
}
