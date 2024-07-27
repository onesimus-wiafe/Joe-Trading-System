package com.joe.trading.user_management.dtos;

import com.joe.trading.user_management.enums.AccountType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserRequestDto {
<<<<<<< HEAD
    @NotEmpty(message = "Username is required")
=======
    @NotEmpty(message = "Name is required")
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
    private String name;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    public CreateUserRequestDto() {
        this.name = null;
        this.email = null;
        this.password = null;
        this.accountType = null;
    }
}
