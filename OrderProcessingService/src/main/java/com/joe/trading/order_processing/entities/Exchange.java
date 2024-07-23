package com.joe.trading.order_processing.entities;

import jakarta.persistence.Entity;
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
    private Long id;

    private String url;

    public Exchange(String url) {
        this.url = url;
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
        return Objects.equals(id, exchange.id) && Objects.equals(url, exchange.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
