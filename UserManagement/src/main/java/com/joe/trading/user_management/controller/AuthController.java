package com.joe.trading.user_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joe.trading.user_management.dtos.LoginRequestDto;
import com.joe.trading.user_management.dtos.LoginResponseDto;
import com.joe.trading.user_management.dtos.RegisterRequestDto;
import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.services.AuthService;
import com.joe.trading.user_management.services.JwtService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        var user = authService.login(loginRequestDto);

        String jwtToken = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new LoginResponseDto(jwtToken, jwtService.getExpirationTime(), userMapper.userToUserResponseDto(user)));
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        var user = authService.register(registerRequestDto);

        return ResponseEntity.ok(userMapper.userToUserResponseDto(user));
    }
}
