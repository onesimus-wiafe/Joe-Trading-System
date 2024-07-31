package com.joe.trading.user_management.services;

import org.springframework.data.domain.Page;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserFilterRequestDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;

public interface UserService {
    User createUser(CreateUserRequestDto createUserDto);

    User getUserById(Long userId) throws ResourceNotFoundException;

    Page<User> getUsers(UserFilterRequestDto filterRequestDto);

    User updateUser(Long userId, UpdateUserDto updatedUser) throws ResourceNotFoundException;
}
