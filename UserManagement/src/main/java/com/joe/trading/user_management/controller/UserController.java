package com.joe.trading.user_management.controller;

import java.util.List;

import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.CreateUserResponseDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exception.ResourceNotFoundException;
import com.joe.trading.user_management.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserResponseDto> createUser(
            @RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        var user = userService.createUser(createUserRequestDto);

        return ResponseEntity.ok(new CreateUserResponseDto(user.getName(), user.getEmail(), user.getAccountType()));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.hasUserId(authentication, #userId)")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) throws ResourceNotFoundException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("{id}")
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable ("id") Long userId, @RequestBody UpdateUserDto updateUser) throws ResourceNotFoundException {
        UpdateUserDto updateUserDto = userService.updateUser(userId,updateUser);
        return ResponseEntity.ok(updateUserDto);
    }

}
