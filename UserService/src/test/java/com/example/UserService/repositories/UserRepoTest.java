package com.example.UserService.repositories;

import com.example.UserService.entities.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void shouldSaveAndFindUserByUsername() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("securePass");

        userRepo.save(user);

        Users userFound = userRepo.findByUsername("testUser");

        assertNotNull(userFound);
        assertEquals(userFound.getUsername(), user.getUsername());
    }
}