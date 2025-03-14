package com.example.UserService.services;

import com.example.UserService.dtos.UserDTO;
import com.example.UserService.models.Users;
import com.example.UserService.repositories.UserRepo;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    private AuthenticationManager authenticationManager;
    private Users user;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1);
        user.setUsername("testUser");
        user.setRole("USER");
    }

    @Test
    void getUserById() {
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.ofNullable(user));
        ResponseEntity<UserDTO> response = userService.getUserById(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getUsername(), response.getBody().getName());
        assertEquals(user.getRole(), response.getBody().getRole());
    }

    @Test
    void addUser() {
    }

    @Test
    void login() {
    }
}