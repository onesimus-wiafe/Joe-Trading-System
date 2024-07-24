package com.joe.trading.order_processing.entities;

import com.joe.trading.order_processing.entities.enums.ExchangeState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Exchange {
    @Id
    private String url;

    @Enumerated(EnumType.STRING)
    private ExchangeState state;

    public Exchange(String url) {
        this.url = url;
        this.state = ExchangeState.SUBSCRIBED;
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exchange exchange = (Exchange) o;
        return Objects.equals(url, exchange.url) && state == exchange.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, state);
    }
}
