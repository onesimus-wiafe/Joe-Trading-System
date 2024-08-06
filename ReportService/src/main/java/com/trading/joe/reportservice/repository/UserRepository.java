package com.trading.joe.reportservice.repository;

import com.trading.joe.reportservice.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users,Long> {

}
