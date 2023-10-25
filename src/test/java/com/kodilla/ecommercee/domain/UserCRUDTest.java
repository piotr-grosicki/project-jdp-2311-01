package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.* ;


@SpringBootTest
public class UserCRUDTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    @DirtiesContext
    public void testCreateUser() {
        // When
        User savedUser = userRepository.save(user);
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        // Then
        assertNotNull(savedUser.getId());
        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    @DirtiesContext
    public void testUpdateUser() {
        // When
        User savedUser = userRepository.save(user);
        savedUser.setPassword("newpassword");
        userRepository.save(savedUser);
        User updatedUser = userRepository.findById(savedUser.getId()).orElse(null);

        // Then
        assertNotNull(updatedUser);
        assertEquals("newpassword", updatedUser.getPassword());
    }

    @Test
    @DirtiesContext
    public void testDeleteUser() {
        // When
        User savedUser = userRepository.save(user);
        userRepository.delete(savedUser);

        // Then
        User deletedUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNull(deletedUser);

    }
}
