package com.neoclan.identitymanagement.service;

import com.neoclan.identitymanagement.dto.Response;
import com.neoclan.identitymanagement.dto.UserUpdateRequestDto;
import com.neoclan.identitymanagement.dto.communication.UserBalanceInfo;

import java.util.List;

public interface UserService {

    List<Response> fetchAll();

    Response fetchSingleUserById(Long userId)   ;

    Response updateUserById(Long id, UserUpdateRequestDto userRequest);

    Response UpdateUserBalanceAfterCredit(UserBalanceInfo userBalanceInfo);
    Response UpdateUserBalanceAfterDebit(UserBalanceInfo userBalanceInfo);

    Response balanceEnquiry(String accountNumber);

    Response nameEnquiry(String accountNumber);


}
