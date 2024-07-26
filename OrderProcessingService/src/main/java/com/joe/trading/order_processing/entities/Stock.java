package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.Ticker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Ticker ticker;

    private Integer quantity;
    private Double stockValue;

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    public Stock(String ticker, Integer quantity){
        this.ticker = Ticker.valueOf(ticker);
        this.quantity = quantity;
    }

    /**
     * If a sell: newValue will be negative.
     * If a buy: newValue will be positive
     * @param newValue the value of new buy or sell Order.
     */
    public void updateStockValue(Double newValue){
        this.stockValue += newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) && ticker == stock.ticker && Objects.equals(quantity, stock.quantity) && Objects.equals(stockValue, stock.stockValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker, quantity, stockValue);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker=" + ticker +
                ", quantity=" + quantity +
                ", stockValue=" + stockValue +
                '}';
    }
}
