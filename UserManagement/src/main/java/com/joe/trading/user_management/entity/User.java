package com.joe.trading.user_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username",nullable = false)
    private String username;

    @Column(name="email",nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "account_type",nullable = false)
    private String AccountType;

    @Column(name = "created_at",nullable = false)
    @CreatedDate
    private Date CreatedAt;

    @Column(name = "updated_at",nullable = false)
    @LastModifiedDate
    private Date UpdatedAt;
}
