package com.joe.trading.user_management.mapper;

import java.util.List;

import com.joe.trading.shared.dtos.UserEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toUserResponseDto(User user);

    List<UserResponseDto> toUserResponseDtos(List<User> users);

    UserEventDto toUserEventDto(User user);
}
