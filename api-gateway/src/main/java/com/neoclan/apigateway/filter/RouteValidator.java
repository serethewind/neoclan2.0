package com.neoclan.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    //configure openEndpoints i.e. end points that will not require authentication with token
    public static final List<String> openApiEndpoints =
            List.of("/api/v2/user/auth/register", "/api/v2/user/auth/login");

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest ->
                    openApiEndpoints.stream().noneMatch(
                            uri -> serverHttpRequest.getURI().getPath().contains(uri));
}
