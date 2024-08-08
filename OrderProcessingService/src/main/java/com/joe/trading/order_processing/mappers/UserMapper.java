package com.joe.trading.order_processing.mappers;

import com.joe.trading.order_processing.entities.User;
import com.joe.trading.shared.dtos.UserEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToUser(UserEventDto userEventDto);
}
