package com.trading.joe.reportservice.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "portfolios")
public class Portfolio {

    @Id
    private Long id;

    private Long portfolio_id;
    private String name;
    private Long user_id;
    private LocalDateTime createdAt;


}
