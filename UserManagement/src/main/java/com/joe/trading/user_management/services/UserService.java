package com.joe.trading.user_management.services;

import java.util.List;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exception.ResourceNotFoundException;

public interface UserService {
    User createUser(CreateUserRequestDto createUserDto);
    User getUserById(Long userId) throws ResourceNotFoundException;
    List<User> getAllUsers();
    User updateUser(Long userId,User updatedUser) throws ResourceNotFoundException;
}
