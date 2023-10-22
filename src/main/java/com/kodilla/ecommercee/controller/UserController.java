package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final List<UserDto> users = new ArrayList<>();
    private final Map<Long, String> userTokens = new HashMap<>();

    public UserController() {
        users.add(new UserDto(1L, "John Smith", "secretpassword1", false, 1L));
        users.add(new UserDto(2L, "Frank Sinatra", "secretpassword2", false, 2L));
        users.add(new UserDto(3L, "Michael Jackson", "secretpassword3", false, 3L));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto newUser) {
        users.add(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (removed) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "{userId}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long id) {
        Optional<UserDto> userDto = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        return userDto.map(user -> {
            user.setIsBlocked(true);
            return ResponseEntity.ok("User blocked successfully");
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/session")
    public ResponseEntity<String> createActiveSession(@RequestBody UserDto loginData) {
        UserDto user = users.stream()
                .filter(u -> u.getUsername().equals(loginData.getUsername()) && u.getPassword().equals(loginData.getPassword()))
                .findFirst()
                .orElse(null);

        if (user != null) {
            String token = generateRandomToken();
            userTokens.put(user.getId(), token);
            scheduleSessionExpiration(user.getId());

            return ResponseEntity.ok("You are logged in." +
                    " Your token is: " + token +
                    " It will expire in 1 hour.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Generowania losowego tokena przy użyciu UUID
    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    // Wygaśnięcie sesji
    private void scheduleSessionExpiration(Long id) {
        long sessionDuration = 3600 * 1000; // 1 hour in milliseconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                userTokens.remove(id);
                System.out.println("Session expired for user " + id);
            }
        }, sessionDuration);
    }
}
