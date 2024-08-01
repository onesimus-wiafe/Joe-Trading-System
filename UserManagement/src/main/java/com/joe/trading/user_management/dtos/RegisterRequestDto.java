package com.joe.trading.user_management.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterRequestDto {
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotEmpty(message = "Name is required")
    private String name;
}
