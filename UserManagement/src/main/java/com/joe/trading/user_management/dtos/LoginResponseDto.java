package com.joe.trading.user_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private Long expiresIn;
    private UserResponseDto user;
}
