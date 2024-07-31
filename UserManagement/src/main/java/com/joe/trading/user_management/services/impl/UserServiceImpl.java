package com.joe.trading.user_management.services.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserFilterRequestDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exceptions.EmailAlreadyExistsException;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;
import com.joe.trading.user_management.repository.UserRepository;
import com.joe.trading.user_management.services.UserService;
import com.joe.trading.user_management.specifications.UserSpecifications;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User does not exist"));
    }

    @Override
    public User createUser(CreateUserRequestDto createUserDto) {
        userRepository.findByEmail(createUserDto.getEmail()).ifPresent(
                user -> {
                    throw new EmailAlreadyExistsException("Email already exists");
                });

        User user = new User();

        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setAccountType(createUserDto.getAccountType());
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));
        user.setPendingDelete(false);

        return userRepository.save(user);
    }

    public Page<User> getUsers(UserFilterRequestDto filterRequestDto) {

        Specification<User> spec = Specification.where(null);

        if (filterRequestDto.getName() != null) {
            spec = spec.and(UserSpecifications.hasName(filterRequestDto.getName()));
        }
        if (filterRequestDto.getEmail() != null) {
            spec = spec.and(UserSpecifications.hasEmail(filterRequestDto.getEmail()));
        }
        if (filterRequestDto.getPendingDelete() != null) {
            spec = spec.and(UserSpecifications.pendingDelete(filterRequestDto.getPendingDelete()));
        }
        if (filterRequestDto.getCreatedFrom() != null && filterRequestDto.getCreatedTo() != null) {
            spec = spec.and(UserSpecifications.createdAtBetween(filterRequestDto.getCreatedFrom(),
                    filterRequestDto.getCreatedTo()));
        }
        if (filterRequestDto.getAccountType() != null) {
            spec = spec.and(UserSpecifications.accountType(filterRequestDto.getAccountType()));
        }

        Pageable pageable = PageRequest.of(
                filterRequestDto.getPage() - 1,
                filterRequestDto.getSize(),
                filterRequestDto.getSortDir().equalsIgnoreCase("desc")
                        ? Sort.by(filterRequestDto.getSortBy()).descending()
                        : Sort.by(filterRequestDto.getSortBy()).ascending());

        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User updateUser(Long userId, UpdateUserDto updatedUser) throws ResourceNotFoundException {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User does not exist"));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAccountType(updatedUser.getAccountType());

        updatedUser.getPassword().ifPresent(
                password -> existingUser.setPasswordHash(passwordEncoder.encode(password)));

        userRepository.save(existingUser);

        return existingUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
