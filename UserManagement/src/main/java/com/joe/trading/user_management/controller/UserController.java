package com.joe.trading.user_management.controller;

import java.util.List;

import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
=======
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.enums.AccountType;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        var user = userService.createUser(createUserRequestDto);

        return ResponseEntity.ok(userMapper.userToUserResponseDto(user));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId) throws ResourceNotFoundException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userMapper.userToUserResponseDto(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
<<<<<<< HEAD
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("{id}")
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable ("id") Long userId, @RequestBody UpdateUserDto updateUser) throws ResourceNotFoundException {
        UpdateUserDto updateUserDto = userService.updateUser(userId,updateUser);
        return ResponseEntity.ok(updateUserDto);
=======
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.usersToUserResponseDtos(users));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long userId,
            @Valid @RequestBody UpdateUserDto updatedUser) throws ResourceNotFoundException, IllegalArgumentException {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var principal = auth.getPrincipal();
        if (principal instanceof User) {
            var user = (User) principal;
            if (user.getAccountType().equals(AccountType.USER) && updatedUser.getAccountType().equals(AccountType.ADMIN)) {
                throw new IllegalArgumentException("You are not authorized to update user to admin");
            }
        } else {
            // TODO: Throw a custom exception, if the principal is not an instance of User
            throw new IllegalArgumentException("You are not authorized to update user");
        }

        User userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userMapper.userToUserResponseDto(userDto));
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
    }

}
