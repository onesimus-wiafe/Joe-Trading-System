package com.trading.joe.reportservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    private Long user_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "accountType", nullable = false)
    private String accountType;

    @Column(name = "pendingDelete", nullable = false)
    private Boolean pendingDelete;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
