package com.trading.joe.reportservice.service;

import com.trading.joe.reportservice.exceptions.ResourceNotFoundException;

public interface ReportingService {
    void createUser() throws ResourceNotFoundException;
}
