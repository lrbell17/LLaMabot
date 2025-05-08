package com.lrbell.llamabot.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    /**
     * JWT token type.
     */
    public static final String TOKEN_TYPE = "Bearer";

    /**
     * Secret key.
     */
    private final Key secretKey;

    /**
     * JWT validity period.
     */
    private final long validityMs;

    /**
     * Constructor.
     *
     * @param secret
     * @param validityMs
     */
    public JwtTokenProvider(
            @Value("${spring.security.jwt.secret}") final String secret,
            @Value("${spring.security.jwt.expiration-ms}") final long validityMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityMs = validityMs;
    }

    /**
     * Create JWT for user based on validity time.
     *
     * @param username
     * @return the JWT token.
     */
    public String createToken(final String username) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + validityMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

}
