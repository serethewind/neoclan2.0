package com.neoclan.apigateway.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private JwtService jwtService; //we will use the token-validate method of jwt service

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest user = null;

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                //if header does not contain the key 'Authorization'
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                //since header contains the key, get the value mapped to that key and assign it to auth header
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                //if header is not null and header starts with 'Bearer ' because value ought to appear as 'Bearer <token string>'
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                //Authheader contains token and token is to be validated
                try {

                    //Instance of jwtService with a validate method is also in the identity management service. note that the secret used in
                    //creating the token in the identity management service is the same secret used in validating.
                    //once token is valid, allow request to the corresponding microservice
                    jwtService.validateToken(authHeader);

                    user = exchange.getRequest().mutate().header("loggedInUser", jwtService.getUsername(authHeader)).build();

                } catch (ExpiredJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
//                    throw new RuntimeException(e);

                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.BAD_REQUEST);
                    return response.setComplete();
                }
            }
            return chain.filter(exchange.mutate().request(user).build());
        });
    }

    public static class Config {

    }
}
