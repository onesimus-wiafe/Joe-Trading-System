package com.joe.trading.user_management.dtos;

import com.joe.trading.user_management.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
