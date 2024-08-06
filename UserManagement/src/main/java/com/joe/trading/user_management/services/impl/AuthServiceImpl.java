package com.joe.trading.user_management.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.joe.trading.user_management.exceptions.UserDeletionException;
import com.joe.trading.user_management.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joe.trading.user_management.dtos.LoginRequestDto;
import com.joe.trading.user_management.dtos.RegisterRequestDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.enums.AccountType;
import com.joe.trading.user_management.exceptions.EmailAlreadyExistsException;
import com.joe.trading.user_management.repository.UserRepository;
import com.joe.trading.user_management.services.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private NatsService natsService;
    private UserMapper userMapper;

    @Override
    public User register(RegisterRequestDto registerRequestDto) {
        userRepository.findByEmail(registerRequestDto.getEmail()).ifPresent(
                user -> {
                    throw new EmailAlreadyExistsException("Email already exists");
                });

        User user = new User();

        user.setName(registerRequestDto.getName());
        user.setEmail(registerRequestDto.getEmail());
        user.setAccountType(AccountType.USER);
        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setPendingDelete(false);

        user = userRepository.save(user);
        // the transactional outbox pattern is ideal for addressing the problem of data consistency across multiple services.
        try {
            natsService.publish(Event.USER_CREATED, userMapper.userEventDto(user));
        } catch (JsonProcessingException e) {
            throw new UserDeletionException("Error registering new user");
        }

        return user;
    }

    @Override
    public User login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()));

        return userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow();
    }
}
