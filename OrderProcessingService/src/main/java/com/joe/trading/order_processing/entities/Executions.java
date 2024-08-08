package com.joe.trading.order_processing.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Executions implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @JsonProperty
    private String timestamp;
    @JsonProperty
    private Double price;
    @JsonProperty
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_book_id")
    private OrderBook orderBook;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Executions that = (Executions) o;
        return Objects.equals(id, that.id) && Objects.equals(timestamp, that.timestamp) && Objects.equals(price, that.price) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, price, quantity);
    }

    @Override
    public String toString() {
        return "Executions{" +
                "timestamp='" + timestamp + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
