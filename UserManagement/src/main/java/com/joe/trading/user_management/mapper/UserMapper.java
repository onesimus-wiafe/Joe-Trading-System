package com.joe.trading.user_management.mapper;

import com.joe.trading.user_management.dtos.UserDto;
import com.joe.trading.user_management.entities.User;

public class UserMapper {

    public static UserDto createUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAccountType(),
                user.getPendingDelete()
        );
    }

}
