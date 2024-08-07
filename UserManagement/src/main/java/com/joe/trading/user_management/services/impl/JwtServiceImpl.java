package com.joe.trading.user_management.services.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.services.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Service
@NoArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.audience}")
    private String audience;

    @Value("${security.jwt.issuer}")
    private String issuer;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public User extractUser(String token) {
        final Claims claims = extractAllClaims(token);
        var user = new User();
        user.setId(((Number) claims.get("id")).longValue());
        user.setName((String) claims.get("name"));
        user.setEmail((String) claims.get("email"));
        user.setAccountType(AccountType.valueOf((String) claims.get("accountType")));
        user.setPendingDelete((Boolean) claims.get("pendingDelete"));
        return user;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        var claims = new HashMap<String, Object>();
        // Put all user data in the token
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("accountType", user.getAccountType());
        claims.put("pendingDelete", user.getPendingDelete());
        return generateToken(claims, user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            User user,
            long expiration) {
        return Jwts
                .builder()
                .header()
                .keyId(generateKeyId())
                .and()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .audience()
                .add(audience)
                .and()
                .issuer(issuer)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateKeyId() {
        var key = getSignInKey();
        try {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(key.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
