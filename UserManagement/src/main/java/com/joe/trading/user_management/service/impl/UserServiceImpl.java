package com.joe.trading.user_management.service.impl;

import com.joe.trading.user_management.entity.User;
import com.joe.trading.user_management.repository.UserRepository;
import com.joe.trading.user_management.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }
}
