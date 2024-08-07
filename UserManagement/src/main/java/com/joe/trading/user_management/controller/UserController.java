package com.joe.trading.user_management.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.shared.dtos.PaginatedResponseDto;
import com.joe.trading.shared.exceptions.ResourceNotFoundException;
import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserFilterRequestDto;
import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;
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

        return ResponseEntity.ok(userMapper.toUserResponseDto(user));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId)
            throws ResourceNotFoundException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userMapper.toUserResponseDto(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<UserResponseDto>> getAllUsers(UserFilterRequestDto filterRequestDto) {
        Page<User> pagedUsers = userService.getUsers(filterRequestDto);

        List<UserResponseDto> userResponseDtos = userMapper.toUserResponseDtos(pagedUsers.getContent());
        PaginatedResponseDto<UserResponseDto> userListResponseDto = new PaginatedResponseDto<>(
                userResponseDtos,
                pagedUsers.getTotalPages(),
                pagedUsers.getTotalElements(),
                pagedUsers.getNumber() + 1);

        return ResponseEntity.ok(userListResponseDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long userId,
            @Valid @RequestBody UpdateUserDto updatedUser) throws ResourceNotFoundException, IllegalArgumentException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (User) auth.getPrincipal();

        if (principal.getAccountType().equals(AccountType.USER)
                && updatedUser.getAccountType().equals(AccountType.ADMIN)) {
            throw new IllegalArgumentException("You are not authorized to update user to admin");
        }

        User userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userMapper.toUserResponseDto(userDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #userId")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) throws ResourceNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
