package com.joe.trading.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PortfolioEventDto {
    private Long id;
    private String name;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;
}
