package com.joe.trading.order_processing.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.shared.auth.AccountType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class PortfolioSpecification {
    private PortfolioSpecification() {
    }

    public static Specification<Portfolio> hasName(String name) {
        return (Root<Portfolio> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Portfolio> hasDescription(String description) {
        return (Root<Portfolio> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .equal(root.get("email"), description);
    }

    public static Specification<Portfolio> createdAtBetween(LocalDateTime start, LocalDateTime end) {
        return (Root<Portfolio> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .between(root.get("createdAt"), start, end);
    }

    public static Specification<Portfolio> accountType(AccountType accountType) {
        return (Root<Portfolio> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .equal(root.get("accountType"), accountType);
    }

    public static Specification<Portfolio> hasUserId(Long userId) {
        return (Root<Portfolio> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .equal(root.get("user").get("id"), userId);
    }
}
