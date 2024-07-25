package com.joe.trading.user_management.services;

import com.joe.trading.user_management.dtos.LoginRequestDto;
import com.joe.trading.user_management.dtos.RegisterRequestDto;
import com.joe.trading.user_management.entities.User;

public interface AuthService {
    public User register(RegisterRequestDto registerRequestDto);

    public User login(LoginRequestDto loginRequestDto);
}
