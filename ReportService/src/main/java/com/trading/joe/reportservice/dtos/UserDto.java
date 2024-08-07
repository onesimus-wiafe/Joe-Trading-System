package com.trading.joe.reportservice.dtos;

import com.joe.trading.shared.dtos.UserEventDto;
import com.trading.joe.reportservice.Status;
import com.trading.joe.reportservice.entities.User;

public class UserDto {

    public User userCreated(UserEventDto userEventDto){
        User user = new User();
        user.setUserId(userEventDto.getId());
        user.setName(userEventDto.getName());
        user.setEmail(userEventDto.getEmail());
        user.setAccountType(user.getAccountType());
        user.setPendingDelete(userEventDto.getPendingDelete());
        user.setCreatedAt(userEventDto.getCreatedAt());
        user.setUpdatedAt(userEventDto.getUpdatedAt());
        user.setAction(Status.CREATED);

        return user;
    }

    public User userUpdated(UserEventDto userEventDto){
        User user = new User();
        user.setUserId(userEventDto.getId());
        user.setName(userEventDto.getName());
        user.setEmail(userEventDto.getEmail());
        user.setAccountType(user.getAccountType());
        user.setPendingDelete(userEventDto.getPendingDelete());
        user.setCreatedAt(userEventDto.getCreatedAt());
        user.setUpdatedAt(userEventDto.getUpdatedAt());
        user.setAction(Status.UPDATED);

        return user;
    }
    public User userDeleted(UserEventDto userEventDto){
        User user = new User();
        user.setUserId(userEventDto.getId());
        user.setName(userEventDto.getName());
        user.setEmail(userEventDto.getEmail());
        user.setAccountType(user.getAccountType());
        user.setPendingDelete(userEventDto.getPendingDelete());
        user.setCreatedAt(userEventDto.getCreatedAt());
        user.setUpdatedAt(userEventDto.getUpdatedAt());
        user.setAction(Status.DELETED);

        return user;
    }
}
