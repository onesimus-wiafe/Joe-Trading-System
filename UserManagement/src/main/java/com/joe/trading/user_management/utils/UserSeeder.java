package com.joe.trading.user_management.utils;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.enums.AccountType;
import com.joe.trading.user_management.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder {
    private UserService userService;

    public UserSeeder(UserService userService){
        var allUsers = userService.getAllUsers();
        if (allUsers.size() == 0) {
            var user = new CreateUserRequestDto("Admin", "admin@admin.com", "password", AccountType.ADMIN);
            userService.createUser(user);
        }
    }
}
