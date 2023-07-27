package com.neoclan.identitymanagement.dto.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBalanceInfo {
    private String accountNumber;
    private BigDecimal transactionAmount;
}
