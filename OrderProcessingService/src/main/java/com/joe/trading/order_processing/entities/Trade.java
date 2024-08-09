package com.joe.trading.order_processing.entities;


import com.joe.trading.order_processing.entities.enums.TradeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    private Double price;
    private String ticker;
    private String side;
    private String tradeType;

    private String exchange;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "trade_id")
    private OrderBook orderBook;

    public Trade(Integer quantity, Double price, String ticker, String side, String tradeType, String exchange){
        this.quantity = quantity;
        this.price = price;
        this.ticker = ticker;
        this.side = side;
        this.tradeType = tradeType;
        this.exchange = exchange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(id, trade.id) && Objects.equals(quantity, trade.quantity) && Objects.equals(price, trade.price) && Objects.equals(ticker, trade.ticker) && Objects.equals(side, trade.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, price, ticker, side);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "quantity=" + quantity +
                ", price=" + price +
                ", ticker='" + ticker + '\'' +
                ", side='" + side + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", status=" + status +
                ", order=" + order +
                ", orderBook=" + orderBook +
                '}';
    }
}
