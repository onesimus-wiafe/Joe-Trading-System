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
    private Long id;
    private String name;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;

}
