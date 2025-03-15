package com.example.UserService.services;

import com.example.UserService.dtos.UserDTO;
import com.example.UserService.entities.Users;
import com.example.UserService.repositories.UserRepo;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Users user;
    @InjectMocks
    private UserService userService;
    @Mock
    private Authentication authentication;
    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1);
        user.setUsername("testUser");
        user.setRole("USER");
        user.setPassword("plainPassword");
    }

    @Test
    void getUserById() {
        when(userRepo.findById(1)).thenReturn(Optional.ofNullable(user));
        ResponseEntity<UserDTO> response = userService.getUserById(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getUsername(), response.getBody().getName());
        assertEquals(user.getRole(), response.getBody().getRole());
    }

    @Test
    void shouldSaveUserSuccessfully() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepo.save(any(Users.class))).thenReturn(user);

        String result = userService.addUser(user);

        assertEquals("User Saved", result);
        assertEquals("hashedPassword", user.getPassword());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    void shouldReturnFailureMessageOnException() {
        doThrow(new RuntimeException("Database error")).when(userRepo).save(any(Users.class));

        String result = userService.addUser(user);

        assertEquals("User save failed ", result);
    }

    @Test
    void shouldReturnFailureMessageOnEncryptionException() {
        when(bCryptPasswordEncoder.encode(anyString())).thenThrow(new RuntimeException("Not Encrypted"));

        String result = userService.addUser(user);

        assertEquals("User save failed ", result);
    }


    @Test
    void loginShouldReturnTokenIfAuthenticated() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).
                thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("mocked-jwt-token");

        String result = userService.login(user);

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken
                        (user.getUsername(), user.getPassword()));
        verify(authentication, times(1)).isAuthenticated();
        verify(jwtService, times(1)).generateToken(user.getUsername());
        assertEquals(result, "mocked-jwt-token");
    }

    @Test
    void loginShouldReturnExceptionIfNotAuthenticated() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).
                thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        String result = userService.login(user);

        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken
                        (user.getUsername(), user.getPassword()));
        verify(authentication, times(1)).isAuthenticated();
        verify(jwtService, times(0)).generateToken(user.getUsername());
        assertEquals(result, "User Not Found.");
    }
}