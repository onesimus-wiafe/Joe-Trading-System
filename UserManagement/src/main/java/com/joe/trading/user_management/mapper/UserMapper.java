package com.joe.trading.user_management.mapper;

import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;

public class UserMapper {

    public static UserResponseDto createUserDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAccountType(),
                user.getPendingDelete(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
