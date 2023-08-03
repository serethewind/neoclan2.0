package com.neoclan.apigateway.filter;

import com.neoclan.apigateway.utils.SecurityConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
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

//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION))
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }

//    public String generateToken(UserDetails userDetails){
//        return generateToken(new HashMap<>(), userDetails);
//    }

    public String generateToken(String username){
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    public String generateToken(Neo4jProperties.Authentication authentication){
//        return Jwts.builder()
//                .setClaims(new HashMap<>())
//                .setSubject(authentication.getName())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION))
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//
//    }

//    public boolean isTokenValid(String token, UserDetails userDetails){
//        String username = getUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

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


    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}