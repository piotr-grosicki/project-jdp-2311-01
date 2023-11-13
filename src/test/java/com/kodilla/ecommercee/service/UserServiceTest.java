package com.kodilla.ecommercee.service;

import com.kodilla.ecommercee.domain.User;
import com.kodilla.ecommercee.domain.UserDto;
import com.kodilla.ecommercee.exception.UnauthorizedException;
import com.kodilla.ecommercee.exception.UserNotFoundException;
import com.kodilla.ecommercee.mapper.UserMapper;
import com.kodilla.ecommercee.repository.UserRepository;
import com.kodilla.ecommercee.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    private UserDto newUser;

    @BeforeEach
    public void setup() {
        newUser = new UserDto();
        newUser.setUsername("testuser");
        newUser.setPassword("password");
        newUser.setTokenExpirationTime(null);
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testGetAllUsers() {
        // Given
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setTokenExpirationTime(LocalTime.from(LocalDateTime.now()));
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setTokenExpirationTime(LocalTime.from(LocalDateTime.now()));
        userRepository.save(user2);

        // When
        List<UserDto> users = userService.getAllUsers();

        // Then
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("password1", users.get(0).getPassword());
        assertEquals("user2", users.get(1).getUsername());
        assertEquals("password2", users.get(1).getPassword());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testGetUserByIdUserNotFound() {
        // When and Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testCreateAndRetrieveUser() {
        // When
        UserDto createdUser = userService.createUser(newUser);

        // Then
        assertNotNull(createdUser.getId());
        UserDto foundUser = userService.getUserById(createdUser.getId());
        assertNotNull(foundUser);
        assertEquals(newUser.getUsername(), foundUser.getUsername());
        assertEquals(newUser.getPassword(), foundUser.getPassword());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testDeleteUser() {
        // When
        UserDto createdUser = userService.createUser(newUser);

        // Then
        assertNotNull(createdUser.getId());
        userService.deleteUser(createdUser.getId());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testBlockUser() {
        // Given
        UserDto createdUser = userService.createUser(newUser);

        // When
        userService.blockUser(createdUser.getId());

        // Then
        UserDto blockedUser = userService.getUserById(createdUser.getId());
        assertNotNull(blockedUser);
        assertTrue(blockedUser.getIsBlocked());
    }

    @Test
    @Transactional
    @DirtiesContext
    void createActiveSession_ValidLoginData_ShouldCreateSession() {
        // Given
        UserDto existingUser = newUser;
        existingUser.setUsername("testuser");
        existingUser.setPassword("password");

        // When
        String result = String.valueOf(userService.createActiveSession(existingUser));

        // Then
        assertNotNull(result);
        if (result.startsWith("You are logged in. Your token is: ")) {

            User user = userRepository.findByUsername(existingUser.getUsername());
            assertNotNull(user);
            assertNotNull(user.getToken());
            assertNotNull(user.getTokenExpirationTime());
        }
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testInvalidLoginPassword() {
        // Given
        userService.createUser(newUser);

        // When
        UserDto loginData = new UserDto();
        loginData.setUsername("testuser");
        loginData.setPassword("invalid_password");

        // Then
        ResponseEntity<String> response = userService.createActiveSession(loginData);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testInvalidLoginUserNotFound() {
        // When
        UserDto loginData = new UserDto();
        loginData.setUsername("non_existent_user");
        loginData.setPassword("password");

        // Then
        ResponseEntity<String> response = userService.createActiveSession(loginData);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void testCreateUserWithDuplicateUsername() {
        // Given
        userService.createUser(newUser);

        // When
        UserDto duplicateUser = new UserDto();
        duplicateUser.setUsername("testuser");
        duplicateUser.setPassword("another_password");

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> userService.createUser(duplicateUser));
    }

    @Test
    @DirtiesContext
    public void testDeleteNonExistentUser() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(999L));
    }

    @Test
    @Transactional
    @DirtiesContext
    public void createActiveSession_InvalidLoginData_ShouldThrowUnauthorizedException() {
        // Given
        UserDto loginData = newUser;
        loginData.setUsername("testUser");
        loginData.setPassword("testPassword");

        // When
        ResponseEntity<String> response = userService.createActiveSession(loginData);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void createActiveSession_LoginTooSoon_ShouldThrowUnauthorizedException() {
        // Given
        UserDto loginData = newUser;
        loginData.setUsername("testUser");
        loginData.setPassword("testPassword");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setTokenExpirationTime(LocalTime.now().minusMinutes(30));
        userRepository.save(user);

        // When
        ResponseEntity<String> response = userService.createActiveSession(loginData);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Login too soon. Please wait before trying again.", response.getBody());
    }

}