package com.neoclan.identitymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDto {

    private String firstName;
    private String lastName;
    private String otherName;
    private String gender;
    private String username;
    private String address;
    private String stateOfOrigin;
    private BigDecimal accountBalance;
    private String email;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String password;
    private LocalDate dateOfBirth;
}
