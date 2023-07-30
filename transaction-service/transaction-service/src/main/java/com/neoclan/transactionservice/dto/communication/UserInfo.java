package com.neoclan.transactionservice.dto.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfo {
    private String accountName;
    private String accountNumber;
    private BigDecimal accountBalance;
}
