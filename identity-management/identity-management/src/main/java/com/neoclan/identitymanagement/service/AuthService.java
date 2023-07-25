package com.neoclan.identitymanagement.service;

import com.neoclan.identitymanagement.dto.Response;
import com.neoclan.identitymanagement.dto.UserLoginRequestDto;
import com.neoclan.identitymanagement.dto.UserRegisterRequestDto;

public interface AuthService {

    Response registerUser (UserRegisterRequestDto userRegisterRequestDto);
    Response loginUser (UserLoginRequestDto userLoginRequestDto);

    Response resetPassword(UserLoginRequestDto userLoginRequestDto);
}
