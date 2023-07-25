package com.neoclan.transactionservice.dto;

import com.neoclan.transactionservice.entity.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransactionDto {
    private TransactionType transactionType;
    private String accountNumber;
    private BigDecimal amount;
}
