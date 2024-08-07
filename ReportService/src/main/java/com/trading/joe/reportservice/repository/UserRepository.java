package com.trading.joe.reportservice.repository;

import com.trading.joe.reportservice.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,Long> {

}
