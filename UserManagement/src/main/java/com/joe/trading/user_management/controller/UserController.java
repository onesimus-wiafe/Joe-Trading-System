package com.joe.trading.user_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @PostMapping
    @RolesAllowed("ADMIN")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
