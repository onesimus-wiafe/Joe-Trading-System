package com.joe.trading.user_management.dtos;

<<<<<<< HEAD
import com.joe.trading.user_management.enums.AccountType;
=======
import java.util.Optional;

import com.joe.trading.user_management.enums.AccountType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
import java.time.LocalDateTime;

=======
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
<<<<<<< HEAD
    private String name;
    private String email;
    //private String passwordHash;
    private AccountType accountType;
    private Boolean pendingDelete;
=======
    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email is invalid")
    private String email;

    private Optional<String> password = Optional.empty();

    private AccountType accountType = AccountType.USER;
>>>>>>> 24209e6aa821187065ccf0f0fa0d0917bc50e156
}
