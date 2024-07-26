package com.joe.trading.order_processing.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    private Long id;

    private Double totalFunds;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    List<Portfolio> portfolios = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    public User(Long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    public void addPortfolio(Portfolio portfolio){
        portfolio.setUser(this);
        this.portfolios.add(portfolio);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", portfolios=" + portfolios +
                '}';
    }
}
