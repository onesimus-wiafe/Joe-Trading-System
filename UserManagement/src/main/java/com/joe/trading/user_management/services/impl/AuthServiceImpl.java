package com.joe.trading.user_management.services.impl;

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

        return userRepository.save(user);
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
