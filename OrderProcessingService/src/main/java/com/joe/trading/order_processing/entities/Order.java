package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.AvailableExchanges;
import com.joe.trading.order_processing.entities.enums.OrderType;
import com.joe.trading.order_processing.entities.enums.Side;
import com.joe.trading.order_processing.entities.enums.Ticker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * This is a user's request to buy/sell a stock.
 * We process this and define it for when we make a trade on the exchange.
 * Based on the kind of trades we make, an order can have many trades that are fulfilled to make it complete.
 *
 */
@Entity(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    private Long id;

    private Ticker ticker;
    private Integer quantity;
    private Double unitPrice;

    @Enumerated(EnumType.STRING)
    private Side side;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private AvailableExchanges exchanges;

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<Trade> trades;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Order
            (Ticker ticker, Integer quantity, Double unitPrice,
             Side side, AvailableExchanges exchanges, OrderType type) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.side = side;
        this.exchanges = exchanges;
        this.orderType = type;
        this.createdDate = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && ticker == order.ticker && Objects.equals(quantity, order.quantity) && Objects.equals(unitPrice, order.unitPrice) && side == order.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker, quantity, unitPrice, side);
    }

    @Override
    public String toString() {
        return "Order{" +
                "ticker=" + ticker +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", side=" + side +
                ", exchanges=" + exchanges +
                ", orderType=" + orderType +
                ", trades=" + trades +
                '}';
    }
}
