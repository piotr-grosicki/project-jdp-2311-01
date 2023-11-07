package com.kodilla.ecommercee.services;

import com.kodilla.ecommercee.domain.User;
import com.kodilla.ecommercee.domain.UserDto;
import com.kodilla.ecommercee.exception.UnauthorizedException;
import com.kodilla.ecommercee.exception.UserNotFoundException;
import com.kodilla.ecommercee.mapper.UserMapper;
import com.kodilla.ecommercee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    public final UserRepository userRepository;
    public final UserMapper userMapper;
    private final Map<Long, String> userTokens = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return userMapper.mapToDto(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    public UserDto createUser(UserDto newUser) {
        User user = userMapper.mapToEntityUser(newUser);
        userRepository.save(user);
        return userMapper.mapToDto(user);
    }

    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    public void blockUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsBlocked(true);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    public String createActiveSession(UserDto loginData) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(loginData.getUsername()));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginData.getPassword())) {
                String token = generateRandomToken();
                userTokens.put(user.getId(), token);
                scheduleSessionExpiration(user.getId());
                return "You are logged in. Your token is: " + token + " It will expire in 1 hour.";
            }
        }
        throw new UnauthorizedException("Invalid username or password");
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

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
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return userMapper.mapToDto(user);
        } else {
            throw new UserNotFoundException("Użytkownik o nazwie " + username + " nie istnieje.");
        }
    }


}


