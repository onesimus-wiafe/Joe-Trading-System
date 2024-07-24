package com.joe.trading.user_management.dtos;

import com.joe.trading.user_management.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserResponseDto {
    private final String username;
    private final String email;
    private final AccountType accountType;
}
