package com.mccasani.security.repository;

import com.mccasani.security.util.JwtUtil;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtUtil jwtUtil;

    public JwtSecurityContextRepository(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = jwtUtil.resolveToken(exchange.getRequest());
        if (token != null && jwtUtil.validateToken(token)) {
            return Mono.just(new SecurityContextImpl(jwtUtil.getAuthentication(token)));
        }
        return Mono.empty();
    }
}
