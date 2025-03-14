package com.mccasani.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Generar una clave segura para HS512
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long expirationTime = 864_000_000; // 10 días

    public String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);
        return new UsernamePasswordAuthenticationToken(username, null, roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public String generateToken(String username, String roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", List.of(roles)); // Convierte el rol en una lista
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512) // Usa la clave segura
                .compact();
    }
}