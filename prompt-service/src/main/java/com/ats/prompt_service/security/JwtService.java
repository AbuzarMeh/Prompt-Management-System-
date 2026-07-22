package com.ats.prompt_service.security;

import com.ats.prompt_service.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    private SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    private void initialize() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a signed JWT for the given username.
     */
    public String generateToken(String username) {

        Date issuedAt = new Date();

        Date expiration = new Date(
                issuedAt.getTime() + jwtProperties.getExpiration()
        );

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Returns the username (subject) from the JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks whether the JWT is valid.
     */
    public boolean isTokenValid(String token) {

        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }

    }

    /**
     * Checks if the token has expired.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts expiration claim.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic claim extractor.
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT and returns all claims.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}