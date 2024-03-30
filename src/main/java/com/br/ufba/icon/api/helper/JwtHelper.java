package com.br.ufba.icon.api.helper;

import com.br.ufba.icon.api.exceptions.AccessDeniedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtHelper {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final int MINUTES = 60;

    public static String generateToken(String username) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String extractUsername(String token) {
        return getBodyToken(token).getSubject();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private static Claims getBodyToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException e) {
            try {
                throw new AccessDeniedException("Access denied: " + e.getMessage());
            } catch (AccessDeniedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static boolean isTokenExpired(String token) {
        Claims claims = getBodyToken(token);
        return claims.getExpiration().before(new Date());
    }

    public static String getTokenExpiration(String token) {
        Claims claims = getBodyToken(token);
        return claims.getExpiration().toString();
    }

}
