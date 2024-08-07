package com.joe.trading.user_management.dtos;

import java.time.LocalDateTime;

import com.joe.trading.shared.auth.AccountType;

import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "Page number must be greater than 0")
    private int page = 1;

    @Min(value = 1, message = "Page size must be greater than 0")
    private int size = 10;
    private String sortBy = "id";

    private String sortDir = "asc";
}
