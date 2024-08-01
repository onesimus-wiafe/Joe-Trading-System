package com.joe.trading.user_management.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joe.trading.shared.events.Event;
import com.joe.trading.shared.nats.NatsService;
import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.entities.Portfolio;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.enums.AccountType;
import com.joe.trading.user_management.exceptions.EmailAlreadyExistsException;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;
import com.joe.trading.user_management.exceptions.UserDeletionException;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.repository.PortfolioRepository;
import com.joe.trading.user_management.repository.UserRepository;
import com.joe.trading.user_management.services.impl.UserServiceImpl;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private NatsService natsService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private CreateUserRequestDto createUserRequestDto;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAccountType(AccountType.USER);
        user.setPasswordHash("hashed_password");

        createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("New User");
        createUserRequestDto.setEmail("new@example.com");
        createUserRequestDto.setPassword("password");
        createUserRequestDto.setAccountType(AccountType.USER);

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Updated User");
        updateUserDto.setEmail("updated@example.com");
        updateUserDto.setAccountType(AccountType.ADMIN);
        updateUserDto.setPassword(Optional.of("new_password"));
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() throws ResourceNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertThat(foundUser).isEqualTo(user);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_shouldCreateAndReturnUser_whenRequestIsValid() throws JsonProcessingException {
        when(userRepository.findByEmail(createUserRequestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createUserRequestDto.getPassword())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(createUserRequestDto);

        assertThat(createdUser).isEqualTo(user);
        verify(userRepository, times(1)).findByEmail(createUserRequestDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(natsService, times(1)).publish(Event.USER_CREATED, userMapper.userEventDto(user));
    }

    @Test
    void createUser_shouldThrowEmailAlreadyExistsException_whenEmailAlreadyExists() throws JsonProcessingException {
        when(userRepository.findByEmail(createUserRequestDto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(createUserRequestDto));
        verify(userRepository, times(1)).findByEmail(createUserRequestDto.getEmail());
        verify(userRepository, times(0)).save(any(User.class));
        verify(natsService, times(0)).publish(any(Event.class), any());
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser_whenRequestIsValid() throws ResourceNotFoundException, JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updateUserDto.getPassword().get())).thenReturn("new_hashed_password");

        User updatedUser = userService.updateUser(1L, updateUserDto);

        assertThat(updatedUser.getName()).isEqualTo(updateUserDto.getName());
        assertThat(updatedUser.getEmail()).isEqualTo(updateUserDto.getEmail());
        assertThat(updatedUser.getAccountType()).isEqualTo(updateUserDto.getAccountType());
        assertThat(updatedUser.getPasswordHash()).isEqualTo("new_hashed_password");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(natsService, times(1)).publish(Event.USER_UPDATED, userMapper.userEventDto(user));
    }

    @Test
    void updateUser_shouldThrowResourceNotFoundException_whenUserDoesNotExist() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, updateUserDto));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(any(User.class));
        verify(natsService, times(0)).publish(any(Event.class), any());
    }

    @Test
    void deleteUser_shouldDeleteUser_whenNoPortfoliosExist() throws ResourceNotFoundException, JsonProcessingException {
        when(portfolioRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
        verify(portfolioRepository, times(1)).findByUserId(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(natsService, times(1)).publish(Event.USER_DELETED, userMapper.userEventDto(user));
    }

    @Test
    void deleteUser_shouldMarkUserAsPendingDelete_whenPortfoliosExist() throws Exception {
        Portfolio portfolio = new Portfolio();
        when(portfolioRepository.findByUserId(1L)).thenReturn(Collections.singletonList(portfolio));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(natsService).publish(any(Event.class), any());

        userService.deleteUser(1L);

        verify(natsService, times(1)).publish(any(Event.class), any());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        assertThat(user.getPendingDelete()).isTrue();
        verify(portfolioRepository, times(1)).findByUserId(1L);
        verify(userRepository, times(0)).deleteById(1L);
        verify(natsService, times(1)).publish(Event.DELETE_PORTFOLIO_REQUEST, portfolio);
    }

    @Test
    void deleteUser_shouldThrowUserDeletionException_whenNatsServiceFails() throws Exception {
        Portfolio portfolio = new Portfolio();
        when(portfolioRepository.findByUserId(1L)).thenReturn(Collections.singletonList(portfolio));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doThrow(new JsonProcessingException("Error") {
        }).when(natsService).publish(any(Event.class), any());

        assertThrows(UserDeletionException.class, () -> userService.deleteUser(1L));
        verify(natsService, times(1)).publish(any(Event.class), any());
        verify(userRepository, times(0)).save(any(User.class));
    }
}
