package com.trading.joe.reportservice.entities;


import com.trading.joe.reportservice.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    private Long portfolio_id;

    private String name;
    private Long user_id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status action;


}
