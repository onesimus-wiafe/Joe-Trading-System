package com.joe.trading.user_management.services;

import java.util.List;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
<<<<<<< HEAD
import com.joe.trading.user_management.dtos.UserDto;
=======
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;

public interface UserService {
    User createUser(CreateUserRequestDto createUserDto);
    User getUserById(Long userId) throws ResourceNotFoundException;
<<<<<<< HEAD
    List<UserDto> getAllUsers();
    UpdateUserDto updateUser(Long userId, UpdateUserDto updatedUser) throws ResourceNotFoundException;
=======
    List<User> getAllUsers();
    User updateUser(Long userId, UpdateUserDto updatedUser) throws ResourceNotFoundException;
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
}
