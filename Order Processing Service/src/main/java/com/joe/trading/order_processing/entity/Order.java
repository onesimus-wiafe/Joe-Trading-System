package com.joe.trading.order_processing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Order {
    @Id
    private Long id;
}
