package com.trading.joe.reportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.trading.joe", "com.joe.trading.shared"})
public class ReportserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportserviceApplication.class, args);
	}

}
