package com.joe.trading.user_management.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> usersToUserResponseDtos(List<User> users);
}
