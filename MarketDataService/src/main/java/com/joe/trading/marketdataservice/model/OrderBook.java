package com.joe.trading.marketdataservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class OrderBook {
    private String orderID;
    private Double price;
    private String product;
    private Integer quantity;
    private Integer cumulatitiveQuantity;
    private Double cumulatitivePrice;
    private String side;
    private String orderType;

    private List<Executions> executions = new ArrayList();

    public OrderBook(String product, String id, Integer quantity, String side, String type) {
        this.product = product;
        this.orderID = id;
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
        return Objects.equals(orderID, orderBook.orderID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderID);
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "orderID=" + orderID +
                "price=" + price +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", cumulatitiveQuantity=" + cumulatitiveQuantity +
                ", side='" + side + '\'' +
                ", type='" + orderType + '\'' +
                ", executions=" + executions +
                '}';
    }
}