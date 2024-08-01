package com.joe.trading.user_management.dtos;

import jakarta.validation.constraints.Email;
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
public class LoginRequestDto {
    @Email(message = "Email must be valid")
    private String email;

    private String password;
}
