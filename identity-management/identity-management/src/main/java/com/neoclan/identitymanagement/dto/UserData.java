package com.neoclan.identitymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserData {

    private String accountNumber;
    private String accountName;
    private BigDecimal accountBalance;

    private String token;
}
