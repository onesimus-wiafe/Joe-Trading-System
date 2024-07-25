package com.joe.trading.order_processing.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderBook {
    @Id
    private String id;
    private Double price;
    private String product;
    private Integer quantity;
    private Integer cumulatitiveQuantity;
    private String side;
    private String orderType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderBook", orphanRemoval = true)
    private List<Executions> executions = new ArrayList();

    @OneToOne
    private Trade trade;

    public OrderBook(String product, String id, Integer quantity, String side, String type) {
        this.product = product;
        this.id = id;
        this.quantity = quantity;
        this.side = side;
        this.orderType = type;
        this.cumulatitiveQuantity = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBook orderBook = (OrderBook) o;
        return Objects.equals(id, orderBook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "price=" + price +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", cumulatitiveQuantity=" + cumulatitiveQuantity +
                ", side='" + side + '\'' +
                ", type='" + orderType + '\'' +
                ", executions=" + executions +
                ", trade=" + trade +
                '}';
    }
}
