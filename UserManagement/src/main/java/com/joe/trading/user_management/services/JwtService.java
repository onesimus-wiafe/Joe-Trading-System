package com.joe.trading.user_management.services;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.joe.trading.user_management.entities.User;

import io.jsonwebtoken.Claims;

public interface JwtService {

    public String extractEmail(String token);

    public User extractUser(String token);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    public String generateToken(User userDetails);

    public String generateToken(Map<String, Object> extraClaims, User userDetails);

    public long getExpirationTime();

    public boolean isTokenValid(String token, UserDetails userDetails);
}
