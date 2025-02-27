package com.example.todoapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "3458934898923840238042482384jkdfgkdfkgjdfkjgrdr9898890";

    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", new ArrayList<>(roles))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token){
        return getClaims(token).getSubject();
    }
    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return claims.get("roles", List.class); // 👈 Extract roles from JWT
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }

}
