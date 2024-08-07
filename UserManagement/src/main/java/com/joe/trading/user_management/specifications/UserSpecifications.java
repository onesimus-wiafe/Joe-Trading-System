package com.joe.trading.user_management.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.user_management.entities.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UserSpecifications {
    private UserSpecifications() {
    }

    public static Specification<User> hasName(String name) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .like(root.get("name"), "%" + name + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .equal(root.get("email"), email);
    }

    public static Specification<User> pendingDelete(Boolean pendingDelete) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .equal(root.get("pendingDelete"), pendingDelete);
    }

    public static Specification<User> createdAtBetween(LocalDateTime start, LocalDateTime end) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .between(root.get("createdAt"), start, end);
    }

    public static Specification<User> accountType(AccountType accountType) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
                .equal(root.get("accountType"), accountType);
    }
}
