package com.neoclan.apigateway.filter;

import com.neoclan.apigateway.utils.SecurityConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Component
public class JwtService {
    public Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T  getClaim(String token, Function<Claims, T> claimsResolver){
        Claims claim = getAllClaims(token);
        return claimsResolver.apply(claim);
    }
    public String getUsername(String token){
        return getClaim(token, Claims::getSubject);
    }
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parse(token);
            return true;
        }
        catch (ExpiredJwtException | MalformedJwtException |SecurityException | IllegalArgumentException e){
            throw new RuntimeException(e);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}