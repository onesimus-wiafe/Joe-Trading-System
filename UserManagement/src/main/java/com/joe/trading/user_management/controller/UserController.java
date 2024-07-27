package com.joe.trading.user_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.CreateUserResponseDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exception.ResourceNotFoundException;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserResponseDto> createUser(
            @RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        var user = userService.createUser(createUserRequestDto);

        return ResponseEntity.ok(new CreateUserResponseDto(user.getName(), user.getEmail(), user.getAccountType()));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) throws ResourceNotFoundException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.usersToUserResponseDtos(users));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable("id") Long userId,
            @Valid @RequestBody UpdateUserDto updatedUser) throws ResourceNotFoundException, IllegalArgumentException {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var principal = auth.getPrincipal();
        if (principal instanceof User) {
            var user = (User) principal;
            if (user.getAccountType().name().equals("USER") && updatedUser.getAccountType().name().equals("ADMIN")) {
                throw new IllegalArgumentException("You are not authorized to update user to admin");
            }
        } else {
            throw new IllegalArgumentException("You are not authorized to update user");
        }

        UpdateUserDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }

}
