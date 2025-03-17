package com.example.Gateway.securityFilter;

import com.example.Gateway.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.function.Predicate;

@ExtendWith(MockitoExtension.class)
class RouteValidatorTest {

    @InjectMocks
    private RouteValidator routeValidator;

    @Mock
    private ServerHttpRequest request;

    @BeforeEach
    void setUp() {
        routeValidator = new RouteValidator();
    }

    @Test
    void testIsSecured_whenRouteIsOpen() {
        when(request.getURI()).thenReturn(java.net.URI.create("/users/login"));
        Predicate<ServerHttpRequest> isSecured = routeValidator.isSecured;
        assertFalse(isSecured.test(request));
    }

    @Test
    void testIsSecured_whenRouteIsSecured() {
        when(request.getURI()).thenReturn(java.net.URI.create("/secure-route"));
        Predicate<ServerHttpRequest> isSecured = routeValidator.isSecured;
        assertTrue(isSecured.test(request));
    }
}
