package com.joe.trading.user_management.dtos;

import java.time.LocalDateTime;

import com.joe.trading.user_management.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserFilterRequestDto {
    private String name;
    private String email;
    private Boolean pendingDelete;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private AccountType accountType;
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";
}
