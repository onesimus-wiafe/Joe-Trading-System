package com.joe.trading.order_processing.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.shared.dtos.UserEventDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToUser(UserEventDto userEventDto);
}
