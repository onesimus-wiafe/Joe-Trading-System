package com.joe.trading.user_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.joe.trading")
public class UserManagementApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserManagementApplication.class, args);
	}

}
