package com.neoclan.identitymanagement.config;

import com.neoclan.identitymanagement.entity.TokenEntity;
import com.neoclan.identitymanagement.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@AllArgsConstructor
public class LogoutService implements LogoutHandler {
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        String jwt = authHeader.substring(7);
        TokenEntity token = tokenRepository.findByToken(jwt).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (token != null){
            token.setExpired(true);
            token.setRevoked(true);
            tokenRepository.save(token);
        }
    }
}
