package com.joe.trading.user_management.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.user_management.controller.UserController;
import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.enums.AccountType;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.services.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserResponseDto userResponseDto;
    private CreateUserRequestDto createUserRequestDto;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("testuser");
        user.setAccountType(AccountType.USER);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("testuser");

        createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("newuser");
        createUserRequestDto.setPassword("password");

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("updateduser");
        updateUserDto.setAccountType(AccountType.ADMIN);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_shouldReturnUserResponseDto_whenRequestIsValid() throws Exception {
        when(userService.createUser(any(CreateUserRequestDto.class))).thenReturn(user);
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_shouldReturnUserResponseDto_whenUserExists() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/v1/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_shouldReturnUserResponseDto_whenRequestIsValid() throws Exception {
        when(userService.updateUser(anyLong(), any(UpdateUserDto.class))).thenReturn(user);
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        mockMvc.perform(put("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturnNoContent_whenUserExists() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("User not found")).when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/v1/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
