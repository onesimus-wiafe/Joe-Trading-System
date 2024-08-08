package com.joe.trading.order_processing.entities.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreatePortfolioRequestDTO {
    @NotEmpty(message = "Name is required")
    private String name;
    private String description;
}
