package com.turkcell.spring_cqrs.core.security.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {
    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(UUID userId, String email)
    {
        return generate(userId, email, List.of());
    }

    public String generate(UUID userId, String email, List<String> roles)
    {
        Instant now = Instant.now();
        return Jwts.builder()
                   .issuer(this.jwtProperties.getIssuer())
                   .subject(userId.toString())
                   .claim("email", email)
                   .claim("roles", roles)
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(now.plusSeconds(this.jwtProperties.getExpirationInSeconds())))
                   .signWith(this.signingKey)
                   .compact();
    } 


    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }   

    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rolesClaim = claims.get("roles");
            if (rolesClaim instanceof List<?> roles) {
                return roles.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .toList();
            }
            return List.of();
        });
    }

    public boolean isTokenValid(String token) {
        try {
            return !extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                            .verifyWith(this.signingKey)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
        return resolver.apply(claims);
    }

}
