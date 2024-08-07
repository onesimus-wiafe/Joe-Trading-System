package com.joe.trading.order_processing.services;

import java.util.function.Function;

import com.joe.trading.order_processing.entities.User;

import io.jsonwebtoken.Claims;

public interface JwtService {

    public User extractUser(String token);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
