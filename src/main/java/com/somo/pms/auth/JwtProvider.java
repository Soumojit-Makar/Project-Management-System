package com.somo.pms.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
@Service

public class JwtProvider {

    private final SecretKey SECRET_KEY;
    public JwtProvider(@Value("${jwt.secrete.key}") String secretKey) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    public  String generateToken( Authentication authentication ) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(60*60*24)))
                .claim("email",authentication.getName())
                .signWith(SECRET_KEY)
                .compact();

    }
    public  String getEmail(String token) {
        return String.valueOf(
                Jwts
                        .parser()
                        .verifyWith(SECRET_KEY)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .get("email")
        );
    }
}
