package com.joe.trading.user_management.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserDto;
import com.joe.trading.user_management.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exceptions.EmailAlreadyExistsException;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;
import com.joe.trading.user_management.repository.UserRepository;
import com.joe.trading.user_management.services.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User does not exist"));
    }

    @Override
    public User createUser(CreateUserRequestDto createUserDto) {
        userRepository.findByEmail(createUserDto.getEmail()).ifPresent(
                user -> {
                    throw new EmailAlreadyExistsException("Email already exists");
                });

        User user = new User();

        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setAccountType(createUserDto.getAccountType());
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));
        user.setPendingDelete(false);

        return userRepository.save(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::createUserDto).toList();
    }

    @Override
<<<<<<< HEAD
    public UpdateUserDto updateUser(Long userId, UpdateUserDto updatedUser) throws ResourceNotFoundException {
=======
    public User updateUser(Long userId, UpdateUserDto updatedUser) throws ResourceNotFoundException {
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User does not exist"));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAccountType(updatedUser.getAccountType());
<<<<<<< HEAD
        //existingUser.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
        existingUser.setPendingDelete(updatedUser.getPendingDelete());
        //existingUser.setCreatedAt(existingUser.getCreatedAt());
        existingUser.setUpdatedAt(LocalDateTime.now());


        userRepository.save(existingUser);

        return updatedUser;
=======

        updatedUser.getPassword().ifPresent(
                password -> existingUser.setPasswordHash(passwordEncoder.encode(password)));

        userRepository.save(existingUser);

        return existingUser;
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
    }
}
