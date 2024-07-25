package com.joe.trading.user_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.CreateUserResponseDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exception.ResourceNotFoundException;
import com.joe.trading.user_management.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(
            @RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        var user = userService.createUser(createUserRequestDto);

        return ResponseEntity.ok(new CreateUserResponseDto(user.getName(), user.getEmail(), user.getAccountType()));
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) throws ResourceNotFoundException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable ("id") Long userId, @RequestBody User existingUser) throws ResourceNotFoundException {
        User user = userService.updateUser(userId,existingUser);
        return ResponseEntity.ok(user);
    }

}
