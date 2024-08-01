package com.joe.trading.user_management.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.user_management.controller.AuthController;
import com.joe.trading.user_management.dtos.LoginRequestDto;
import com.joe.trading.user_management.dtos.LoginResponseDto;
import com.joe.trading.user_management.dtos.RegisterRequestDto;
import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.services.AuthService;
import com.joe.trading.user_management.services.JwtService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserResponseDto userResponseDto;
    private LoginRequestDto loginRequestDto;
    private RegisterRequestDto registerRequestDto;
    private LoginResponseDto loginResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Test User");

        loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        loginRequestDto.setPassword("password");

        registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setName("New User");
        registerRequestDto.setEmail("new@example.com");
        registerRequestDto.setPassword("password");

        loginResponseDto = new LoginResponseDto("jwtToken", 3600L, userResponseDto);
    }

    @Test
    @WithMockUser
    void login_shouldReturnLoginResponseDto_whenRequestIsValid() throws Exception {
        when(authService.login(any(LoginRequestDto.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponseDto)));
    }

    @Test
    @WithMockUser
    void register_shouldReturnUserResponseDto_whenRequestIsValid() throws Exception {
        when(authService.register(any(RegisterRequestDto.class))).thenReturn(user);
        when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDto)));
    }

    @Test
    @WithMockUser
    void login_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {
        loginRequestDto.setEmail("invalid-email"); // Invalid email format

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void register_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {
        registerRequestDto.setEmail("invalid-email"); // Invalid email format

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
