package com.joe.trading.order_processing.entity;

import com.joe.trading.order_processing.entity.enums.PortfolioState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.lang.annotation.RequiredTypes;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="portfolio_id")
    private List<Stock> stocks = new ArrayList<>();

    private Double portfolioValue = (double) 0;
    private String portfolioName;
    private PortfolioState state;

    public Portfolio(String portfolioName){
        this.portfolioName = portfolioName;
        this.state = PortfolioState.ACTIVE;
    }

    public void addStock(Stock stock) {
        this.stocks.add(stock);
    }

    public void increaseValue(Double value){
        this.portfolioValue += value;
    }
}
