package com.neoclan.transactionservice.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransactionDto {
    private String transactionType;
    private String accountNumber;
    private BigDecimal amount;
}
