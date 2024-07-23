package com.joe.trading.order_processing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    List<Portfolio> portfolios;

    private Boolean isActive;

    private Long authID;

    public User(Long authID){
        this.portfolios = new ArrayList<>();
        this.isActive = true;
        this.authID = authID;
    }

    public User() {}
}
