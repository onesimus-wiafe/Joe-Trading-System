package com.trading.joe.reportservice.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Users {

    @Id
    private Long id;

    private Long user_id;
    private String name;
    private String email;
    private String accountType;
    private Boolean pendingDelete;
    private LocalDateTime createdAt;
}
