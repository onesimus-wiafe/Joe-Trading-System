package com.joe.trading.user_management.dtos;

import java.util.List;

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
public class UserListResponseDto {
    private List<UserResponseDto> users;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
