package com.trading.joe.reportservice.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.joe.trading.shared.auth.AccountType;
import com.trading.joe.reportservice.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private Long userId;
    private String name;
    private String email;
    private AccountType accountType;
    private Boolean pendingDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status action;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authority = new SimpleGrantedAuthority("ROLE_" + accountType.name());
        return List.of(authority);
    }

    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
