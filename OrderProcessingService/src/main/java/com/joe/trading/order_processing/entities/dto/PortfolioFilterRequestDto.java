package com.joe.trading.order_processing.entities.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PortfolioFilterRequestDto {
    private String name;
    private String description;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;

    @Min(value = 1, message = "Page number must be greater than 0")
    private int page = 1;

    @Min(value = 1, message = "Page size must be greater than 0")
    private int size = 10;
    private String sortBy = "id";

    private String sortDir = "asc";
}
