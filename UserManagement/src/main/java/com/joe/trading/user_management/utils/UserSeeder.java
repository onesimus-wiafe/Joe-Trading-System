package com.joe.trading.user_management.utils;

import org.springframework.stereotype.Component;

import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UserFilterRequestDto;
import com.joe.trading.user_management.services.UserService;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserSeeder {
    private UserService userService;

    @PostConstruct
    public void seedUsers() {
        var allUsers = userService.getUsers(new UserFilterRequestDto());
        if (allUsers.isEmpty()) {
            var user = new CreateUserRequestDto("Admin", "admin@admin.com", "password", AccountType.ADMIN);
            userService.createUser(user);
        }
    }

}
