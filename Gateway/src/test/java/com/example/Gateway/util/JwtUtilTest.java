package com.example.Gateway.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private static final String SECRET_KEY = "test-secret";

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

//    @Test
//    void testGenerateAndValidateToken() {
//        String token = jwtUtil.generateToken("testUser");
//        assertNotNull(token);
//        assertTrue(jwtUtil.validateToken(token));
//    }
//
//    @Test
//    void testExtractUsername() {
//        String token = jwtUtil.generateToken("testUser");
//        assertEquals("testUser", jwtUtil.extractUserName(token));
//    }
//
//    @Test
//    void testExtractExpiration() {
//        String token = jwtUtil.generateToken("testUser");
//        Date expiration = jwtUtil.extractExpiration(token);
//        assertNotNull(expiration);
//        assertTrue(expiration.after(new Date()));
//    }
}
