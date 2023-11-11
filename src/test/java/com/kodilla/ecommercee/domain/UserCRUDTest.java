package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

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
    @Test
    @DirtiesContext
    @Transactional
    public void testUserOrderRelation() {

        Order order = new Order();
        order.setStatus("New");

        user.getOrderList().add(order);

        // When
        User savedUser = userRepository.save(user);

        // Then
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(foundUser);
        assertEquals(1, foundUser.getOrderList().size());
        assertEquals("New", foundUser.getOrderList().get(0).getStatus());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testUserCartRelation() {

        Cart cart = new Cart();

        user.getCartList().add(cart);

        // When
        User savedUser = userRepository.save(user);

        // Then
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(foundUser);
        assertNotNull(foundUser.getCartList());

    }
    @Test
    @DirtiesContext
    public void testFindByUsername() {
        //When
        userRepository.save(user);
        User foundUser = userRepository.findByUsername("testuser");

        //Then
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("password", foundUser.getPassword());
    }

    @Test
    @DirtiesContext
    public void testFindByUsernameWhenUserNotExists() {
        //When
        User foundUser = userRepository.findByUsername("nonexistentuser");

        //Then
        assertNull(foundUser);
    }

}
