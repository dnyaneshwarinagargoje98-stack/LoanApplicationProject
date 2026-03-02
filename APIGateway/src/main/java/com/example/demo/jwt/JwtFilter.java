package com.example.demo.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter {

	@Autowired
	private JwtUtil jwtutil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
        if (
            path.equals("/auth/login") ||
            path.equals("/customer/register")
        ) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtutil.validateAndGetClaims(token);
            
            String username = claims.getSubject();
            String role = claims.get("role").toString();
            if (
                    path.startsWith("/customer/assign-role") ||
                    path.startsWith("/customer/deleteUser") ||
                    path.startsWith("/customer/paging-users")
            ) {
                if (!"ADMIN".equals(role)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
                    ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-Username", claims.getSubject())
                    .header("X-Role", claims.get("role").toString())
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
