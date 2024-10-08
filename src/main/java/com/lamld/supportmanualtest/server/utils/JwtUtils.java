package com.lamld.supportmanualtest.server.utils;

import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.data.auth.TokenInfo;
import com.lamld.supportmanualtest.server.data.type.SellerRoleEnum;
import com.lamld.supportmanualtest.server.entities.Account;
import com.lamld.supportmanualtest.server.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@NoArgsConstructor
@Log4j2
public class JwtUtils {

    @Value("${spring.security.jwt.secret}")
    private String SECRET_KEY;
    @Value("${spring.security.jwt.expiration}")
    private long EXPIRATION_TIME; // In milliseconds

    public TokenInfo createAccessToken(Account account) {
        Key key = getSignKey();

        JwtBuilder builder = Jwts.builder()
            .setClaims(Jwts.claims().setSubject(account.getUsername()))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256);

        AccountInfo accountInfo = new AccountInfo(account);
        if (accountInfo.getAccountId() != null) {
            builder.claim("accountId", accountInfo.getAccountId());
        }
        if (accountInfo.getUsername()!= null) {
            builder.claim("username", accountInfo.getUsername());
        }

        if (accountInfo.getRole() != null) {
            builder.claim("role", accountInfo.getRole());
        }
        // Create access token and initialize TokenInfo object
        String token = builder.compact();
        return new TokenInfo(token, EXPIRATION_TIME / 1000, null, account.getStatus()); // expiresIn in seconds
    }

    public Claims parseToken(String token) {
        try {
            Key key = getSignKey();
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            log.error("Failed to parse JWT token", e);
            throw new AuthenticationException("Invalid JWT token");
        }
    }

    public AccountInfo getUserInfo(String token) {
        // Parse the JWT token to extract claims
        Claims claims = parseToken(token);

        // Extract values from claims with null checks
        Integer accountId = claims.get("accountId", Integer.class);
        String roleStr = claims.get("role", String.class);
        SellerRoleEnum role = roleStr != null ? SellerRoleEnum.valueOf(roleStr) : null;

        // Build and return the SellerInfo object using the extracted claims
        return AccountInfo.builder()
            .accountId(accountId)
            .username(claims.getSubject()) // 'sub' field in JWT corresponds to the username
            .role(role)
            .build();
    }
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean validateToken(String authToken) {
        if(authToken == null || authToken.isEmpty()) return false;
        try {
            Key key = getSignKey();
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
