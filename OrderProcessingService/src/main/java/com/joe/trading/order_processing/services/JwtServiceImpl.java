package com.joe.trading.order_processing.services;

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.shared.auth.AccountType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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
