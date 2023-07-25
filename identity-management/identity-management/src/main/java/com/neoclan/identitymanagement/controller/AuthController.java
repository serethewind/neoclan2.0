package com.neoclan.identitymanagement.controller;

import com.neoclan.identitymanagement.config.LogoutService;
import com.neoclan.identitymanagement.dto.LogoutResponseDto;
import com.neoclan.identitymanagement.dto.Response;
import com.neoclan.identitymanagement.dto.UserLoginRequestDto;
import com.neoclan.identitymanagement.dto.UserRegisterRequestDto;
import com.neoclan.identitymanagement.service.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/auth/")
@AllArgsConstructor
public class AuthController {

    private AuthServiceImpl authService;
    private LogoutService logoutService;

    @PostMapping("login")
    public ResponseEntity<Response> login (@RequestBody UserLoginRequestDto userLoginRequestDto){
        return ResponseEntity.ok(authService.loginUser(userLoginRequestDto));
    }

    @PostMapping("register")
    public ResponseEntity<Response> register (@RequestBody UserRegisterRequestDto userRegisterRequestDto){
        return new ResponseEntity<>( authService.registerUser(userRegisterRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("logout")
    public ResponseEntity<LogoutResponseDto> logout (HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication){
        logoutService.logout(request, response, authentication);
        LogoutResponseDto logoutResponseDto = LogoutResponseDto.builder().response("Logout successful").build();
        return ResponseEntity.ok(logoutResponseDto);

    }
}
