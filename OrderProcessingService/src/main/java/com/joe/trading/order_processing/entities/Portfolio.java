package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.dto.PortfolioResponseDTO;
import com.joe.trading.order_processing.entities.enums.PortfolioState;
import com.joe.trading.order_processing.entities.enums.Ticker;
import com.joe.trading.shared.dtos.PortfolioEventDto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "portfolio")
    private Set<Stock> stocks =new HashSet<>(Ticker.values().length);

    @Column(name = "value", nullable = false)
    private Double value = (double) 0;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PortfolioState state = PortfolioState.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Portfolio(String name) {
        this.name = name;
        this.state = PortfolioState.ACTIVE;
    }

    public Portfolio(String name, PortfolioState state) {
        this.name = name;
        this.state = state;
    }

    public void addStock(Stock stock) {
        this.stocks.add(stock);
    }

    public void updateValue(Double value) {
        this.value += value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(id, portfolio.id) && Objects.equals(value, portfolio.value)
                && Objects.equals(name, portfolio.name) && state == portfolio.state
                && Objects.equals(user, portfolio.user);
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "state=" + state +
                ", name='" + name + '\'' +
                ", portfolioValue=" + value +
                ", stocks=" + stocks +
                '}';
    }

    public PortfolioEventDto toPortfolioEventDto() {
        return new PortfolioEventDto(id, name, description, user.getId(), createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, name, state, user);
    }
}
