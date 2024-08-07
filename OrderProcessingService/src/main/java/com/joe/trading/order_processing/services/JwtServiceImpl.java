package com.joe.trading.order_processing.services;

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

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.shared.auth.AccountType;

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

    public User extractUser(String token) {
        final Claims claims = extractAllClaims(token);
        var user = new User();
        user.setId(((Number) claims.get("id")).longValue());
        user.setAccountType(AccountType.valueOf((String) claims.get("accountType")));
        return user;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
