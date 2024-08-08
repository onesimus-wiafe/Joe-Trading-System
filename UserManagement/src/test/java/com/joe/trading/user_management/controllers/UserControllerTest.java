package com.joe.trading.user_management.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.trading.shared.auth.AccountType;
import com.joe.trading.shared.dtos.PaginatedResponseDto;
import com.joe.trading.user_management.controller.UserController;
import com.joe.trading.user_management.dtos.CreateUserRequestDto;
import com.joe.trading.user_management.dtos.UpdateUserDto;
import com.joe.trading.user_management.dtos.UserFilterRequestDto;
import com.joe.trading.user_management.dtos.UserResponseDto;
import com.joe.trading.user_management.entities.User;
import com.joe.trading.user_management.exceptions.ResourceNotFoundException;
import com.joe.trading.user_management.mapper.UserMapper;
import com.joe.trading.user_management.services.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserController.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User clientUser;
    private User adminUser;
    private UserResponseDto userResponseDto;
    private CreateUserRequestDto createUserRequestDto;
    private UpdateUserDto updateUserDto;
    private Page<User> pagedUsers;
    private PaginatedResponseDto<UserResponseDto> paginatedResponseDto;

    @Autowired
    private UserController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @BeforeEach
    void setUp() {
        clientUser = new User();
        clientUser.setId(1L);
        clientUser.setName("clientuser");
        clientUser.setEmail("clientuser@email.com");
        clientUser.setAccountType(AccountType.USER);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setName("adminuser");
        adminUser.setEmail("adminuser@email.com");
        adminUser.setAccountType(AccountType.ADMIN);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("clientuser");

        createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("newuser");
        createUserRequestDto.setPassword("password");
        createUserRequestDto.setAccountType(AccountType.USER);
        createUserRequestDto.setEmail("testuser@email.com");

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("updateduser");
        updateUserDto.setAccountType(AccountType.ADMIN);
        updateUserDto.setEmail("newtestuser@email.com");

        pagedUsers = new PageImpl<>(Collections.singletonList(clientUser));
        paginatedResponseDto = new PaginatedResponseDto<>(Collections.singletonList(userResponseDto), 1, 1L, 0);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_shouldReturnUserResponseDto_whenRequestIsValid() throws Exception {
        when(userService.createUser(any(CreateUserRequestDto.class))).thenReturn(clientUser);
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(createUserRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_shouldReturnUserResponseDto_whenUserExists() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(clientUser);
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
    void getAllUsers_shouldReturnPaginatedResponseDto_whenRequestIsValid() throws Exception {
        when(userService.getUsers(any(UserFilterRequestDto.class))).thenReturn(pagedUsers);
        when(userMapper.usersToUserResponseDtos(anyList())).thenReturn(Collections.singletonList(userResponseDto));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(paginatedResponseDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_shouldReturnUserResponseDto_whenRequestIsValid() throws Exception {
        when(userService.updateUser(anyLong(), any(UpdateUserDto.class))).thenReturn(clientUser);
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(userResponseDto);

        mockMvc.perform(put("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(adminUser))
                .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponseDto)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturnNoContent_whenUserExists() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", 1L)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("User not found")).when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/v1/users/{id}", 1L)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }
}
