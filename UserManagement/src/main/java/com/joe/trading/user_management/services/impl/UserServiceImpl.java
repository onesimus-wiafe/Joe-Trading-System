package com.joe.trading.user_management.services.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exception.ResourceNotFoundException;
import com.joe.trading.user_management.exceptions.EmailAlreadyExistsException;
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
        if (userRepository.findByEmail(createUserDto.getEmail()) != null) {
            throw new EmailAlreadyExistsException("User with this email already exists");
        }

        User user = new User();

        user.setName(createUserDto.getUsername());
        user.setEmail(createUserDto.getEmail());
        user.setAccountType(createUserDto.getAccountType());
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));
        user.setPendingDelete(false);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long userId, User user) throws ResourceNotFoundException {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User does not exist"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAccountType(user.getAccountType());
        existingUser.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        existingUser.setPendingDelete(false);

        return userRepository.save(existingUser);
    }


}
