package com.server.app.config;

import com.server.app.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JsonWebToken {

    @Value("${security.jwt.expiration-time}")
    private long tokenTime;

    @Value("${security.jwt.secret-key}")
    private String tokenSecret;

    private SecretKey getTokenKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(User user) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("id", user.getId());

        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + tokenTime))
                .signWith(getTokenKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getTokenKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException exception) {
            return null;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = extractClaims(token);

        if (claims == null) {
            return null;
        }

        Number userId = claims.get("id", Number.class);

        return userId != null ? userId.longValue() : null;
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);

        return claims == null
                || claims.getExpiration() == null
                || claims.getExpiration().before(new Date());
    }
}