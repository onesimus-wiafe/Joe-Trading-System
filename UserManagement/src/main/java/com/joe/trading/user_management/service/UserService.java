package com.joe.trading.user_management.service;

import com.joe.trading.user_management.entity.User;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId);

}
