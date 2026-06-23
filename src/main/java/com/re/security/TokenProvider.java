package com.re.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {
    @Value("${app.jwt.access-expires-in-mil-seconds}")
    private String accessExpInMil;
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    private SecretKey secretKey;

    @PostConstruct
    void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateAccessToken(Authentication authentication) {
        return generateAccessTokenFromUsername(authentication.getName());
    }

    public String generateAccessTokenFromUsername(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessExpInMil);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }
}
