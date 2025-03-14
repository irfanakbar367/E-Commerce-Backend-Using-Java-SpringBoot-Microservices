package com.example.UserService.controllers;

import com.example.UserService.SecurityConfig.MyUserDetailsService;
import com.example.UserService.SecurityConfig.SecurityConfig;
import com.example.UserService.dtos.UserDTO;
import com.example.UserService.models.Users;
import com.example.UserService.repositories.UserRepo;
import com.example.UserService.services.JwtService;
import com.example.UserService.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void addUser_ShouldReturnSuccessMessage() throws Exception {
        Users inputUser = new Users();
        inputUser.setUsername("Irfan");
        inputUser.setPassword("password123");
        inputUser.setRole("USER");

        when(userService.addUser(any(Users.class))).thenReturn("User Saved");

        mockMvc.perform(post("/users/addUser")
                        .content(objectMapper.writeValueAsString(inputUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void login_ShouldReturnToken() throws Exception {
        Users loginUser = new Users();
        loginUser.setUsername("Irfan");
        loginUser.setPassword("password123");

        when(userService.login(any(Users.class))).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/users/login")
                        .content(objectMapper.writeValueAsString(loginUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));

    }

    @Test
    void validateToken_ShouldReturnUser() throws Exception {
        String token = "mocked-token";
        String username = "Irfan";
        Users mockUser = new Users();
        mockUser.setUsername(username);

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(myUserDetailsService.loadUserByUsername(username)).thenReturn(Mockito.mock(UserDetails.class));
        when(jwtService.validateToken(eq(token), any(UserDetails.class))).thenReturn(true);
        when(userRepo.findByUsername(username)).thenReturn(mockUser);

        mockMvc.perform(get("/users/validateToken")
                        .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        int userId = 1;
        UserDTO mockUser = new UserDTO();
        mockUser.setName("Irfan");
        mockUser.setRole("ROLE_USER");

        when(userService.getUserById(userId)).thenReturn(ResponseEntity.ok(mockUser));

        mockMvc.perform(get("/users/getUserById")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Irfan"));
    }
}
