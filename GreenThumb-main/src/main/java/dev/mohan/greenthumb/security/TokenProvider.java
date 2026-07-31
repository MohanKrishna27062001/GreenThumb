package dev.mohan.greenthumb.security;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

    private final SecretKey key;
    private final long validityMs;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.expiration-ms}") long validityMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.validityMs = validityMs;
    }

    public String generateToken(Authentication authentication) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        return Jwts.builder()
                .subject(authentication.getName())              // the email
                .claim("roles", roles)                          // their roles
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validityMs))
                .signWith(key)                                  // sign → tamper-proof
                .compact();                                     // produce the string
    }

    // Is this token genuine and unexpired? (verifies the signature with our key)
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;   // bad signature, expired, tampered, or malformed
        }
    }

    // Unpack the token into a Spring "Authentication" (who + their roles)
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();

        String email = claims.getSubject();                       // the "sub"
        String rolesString = claims.get("roles", String.class);   // "ROLE_USER,..."

        List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
                .filter(r -> !r.isBlank())
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 2nd arg (credentials) is null — the token already proved identity, no password needed
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}