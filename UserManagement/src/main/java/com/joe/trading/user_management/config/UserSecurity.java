package com.joe.trading.user_management.config;

import org.springframework.stereotype.Component;

import com.joe.trading.user_management.entities.User;

import org.springframework.security.core.Authentication;

@Component("userSecurity")
public class UserSecurity {
    public boolean hasUserId(Authentication authentication, Long userId) {
        var user = (User) authentication.getPrincipal();

        return user.getId().equals(userId);
    }
}
