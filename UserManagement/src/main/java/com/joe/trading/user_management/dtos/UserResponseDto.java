package com.joe.trading.user_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.joe.trading.shared.auth.AccountType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private AccountType accountType;
    private Boolean pendingDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
