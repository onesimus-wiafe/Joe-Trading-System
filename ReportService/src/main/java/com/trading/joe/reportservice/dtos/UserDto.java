package com.trading.joe.reportservice.dtos;

import com.joe.trading.shared.dtos.UserEventDto;
import com.trading.joe.reportservice.Status;
import com.trading.joe.reportservice.entities.Users;

public class UserDto {

    public Users userCreated(UserEventDto userEventDto){
        Users user = new Users();
        user.setUser_id(userEventDto.getId());
        user.setName(userEventDto.getName());
        user.setEmail(userEventDto.getEmail());
        user.setAccountType(user.getAccountType());
        user.setPendingDelete(userEventDto.getPendingDelete());
        user.setCreatedAt(userEventDto.getCreatedAt());
        user.setUpdatedAt(userEventDto.getUpdatedAt());
        user.setAction(Status.CREATED);

        return user;
    }

    public Users userUpdated(UserEventDto userEventDto){
        Users user = new Users();
        user.setUser_id(userEventDto.getId());
        user.setName(userEventDto.getName());
        user.setEmail(userEventDto.getEmail());
        user.setAccountType(user.getAccountType());
        user.setPendingDelete(userEventDto.getPendingDelete());
        user.setCreatedAt(userEventDto.getCreatedAt());
        user.setUpdatedAt(userEventDto.getUpdatedAt());
        user.setAction(Status.UPDATED);

        return user;
    }
    public Users userDeleted(UserEventDto userEventDto){
        Users user = new Users();
        user.setUser_id(userEventDto.getId());
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
