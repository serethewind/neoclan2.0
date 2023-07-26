package com.neoclan.identitymanagement.service;

import com.neoclan.identitymanagement.dto.Response;
import com.neoclan.identitymanagement.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {

    List<Response> fetchAll();

    Response fetchSingleUserById(Long userId)   ;

    Response updateUserById(Long id, UserUpdateRequestDto userRequest);

    Response balanceEnquiry(String accountNumber);

    Response nameEnquiry(String accountNumber);


}
