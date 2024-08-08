package com.joe.trading.order_processing.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderBook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @JsonProperty
    private String id;
    @JsonProperty
    private Double price;
    @JsonProperty
    private String product;
    @JsonProperty
    private Integer quantity;
    @JsonProperty
    private Integer cumulatitiveQuantity;
    @JsonProperty
    private String side;
    @JsonProperty
    private String orderType;
    @JsonProperty
    private String exchange;

    @JsonProperty
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderBook", orphanRemoval = true)
    private List<Executions> executions = new ArrayList<>();

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
