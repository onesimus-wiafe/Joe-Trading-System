package com.joe.trading.user_management.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.shared.dtos.UserEventDto;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.joe.trading.user_management.dtos.LoginRequestDto;
import com.joe.trading.user_management.dtos.RegisterRequestDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.enums.AccountType;
import com.joe.trading.user_management.exceptions.EmailAlreadyExistsException;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.repository.UserRepository;
import com.joe.trading.user_management.services.impl.AuthServiceImpl;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NatsService natsService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private RegisterRequestDto registerRequestDto;
    private LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashed_password");
        user.setAccountType(AccountType.USER);

        registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setName("New User");
        registerRequestDto.setEmail("new@example.com");
        registerRequestDto.setPassword("password");

        loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        loginRequestDto.setPassword("password");
    }

    @Test
    void register_shouldCreateAndReturnUser_whenRequestIsValid() throws JsonProcessingException {
        when(userRepository.findByEmail(registerRequestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequestDto.getPassword())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        var userEventDto = new UserEventDto();
        when(userMapper.userEventDto(any(User.class))).thenReturn(userEventDto);

        User createdUser = authService.register(registerRequestDto);

        assertThat(createdUser).isEqualTo(user);
        verify(userRepository, times(1)).findByEmail(registerRequestDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(natsService, times(1)).publish(Event.USER_CREATED, userEventDto);
    }

    @Test
    void register_shouldThrowEmailAlreadyExistsException_whenEmailAlreadyExists() {
        when(userRepository.findByEmail(registerRequestDto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(registerRequestDto));
        verify(userRepository, times(1)).findByEmail(registerRequestDto.getEmail());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void login_shouldReturnUser_whenCredentialsAreValid() {
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any())).thenReturn(null);

        User loggedInUser = authService.login(loginRequestDto);

        assertThat(loggedInUser).isEqualTo(user);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(loginRequestDto.getEmail());
    }

    @Test
    void login_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any())).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> authService.login(loginRequestDto));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(loginRequestDto.getEmail());
    }
}
