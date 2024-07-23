package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.PortfolioState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
    @JoinColumn(name="portfolio_id")
    private List<Stock> stocks = new ArrayList<>();

    private Double portfolioValue = (double) 0;
    private String portfolioName;
    private PortfolioState state;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    public Portfolio(String portfolioName){
        this.portfolioName = portfolioName;
        this.state = PortfolioState.ACTIVE;
    }

    public void addStock(Stock stock) {
        this.stocks.add(stock);
    }

    public void updateValue(Double value){
        this.portfolioValue += value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(id, portfolio.id) && Objects.equals(portfolioValue, portfolio.portfolioValue) && Objects.equals(portfolioName, portfolio.portfolioName) && state == portfolio.state && Objects.equals(user, portfolio.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, portfolioValue, portfolioName, state, user);
    }
}
