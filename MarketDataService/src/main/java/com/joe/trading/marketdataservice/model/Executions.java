package com.joe.trading.marketdataservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Executions {
    private Integer quantity;
    private String timestamp;
    private Double price;

    @Override
    public String toString() {
        return "Executions{" +
                "quantity=" + quantity +
                ", timestamp=" + timestamp +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Executions that)) return false;
        return Objects.equals(quantity, that.quantity) && Objects.equals(timestamp, that.timestamp) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, timestamp, price);
    }
}
