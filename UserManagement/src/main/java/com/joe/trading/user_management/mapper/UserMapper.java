package com.joe.trading.user_management.mapper;

<<<<<<< HEAD
import com.joe.trading.user_management.dtos.UserDto;
import com.joe.trading.user_management.entities.User;

public class UserMapper {

    public static UserDto createUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAccountType(),
                user.getPendingDelete(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

=======
import java.util.List;

import org.mapstruct.Mapper;

import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToUserResponseDto(User user);

    User userResponseDtoToUser(UserResponseDto userResponseDto);

    List<UserResponseDto> usersToUserResponseDtos(List<User> users);

    List<User> userResponseDtosToUsers(List<UserResponseDto> userResponseDtos);
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
}
