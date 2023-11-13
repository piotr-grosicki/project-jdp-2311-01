package com.kodilla.ecommercee.services;

import com.kodilla.ecommercee.domain.User;
import com.kodilla.ecommercee.domain.UserDto;
import com.kodilla.ecommercee.exception.UnauthorizedException;
import com.kodilla.ecommercee.exception.UserNotFoundException;
import com.kodilla.ecommercee.mapper.UserMapper;
import com.kodilla.ecommercee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    public final UserRepository userRepository;
    public final UserMapper userMapper;

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
        if (userRepository.findByUsername(newUser.getUsername()) != null) {
            throw new DataIntegrityViolationException("User named " + newUser.getUsername() + " already exists.");
        }
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

    @Transactional
    public ResponseEntity<String> createActiveSession(UserDto loginData) {
        User user = userRepository.findByUsername(loginData.getUsername());

        if (user != null && loginData.getPassword() != null && user.getPassword().equals(loginData.getPassword())) {
            LocalTime now = LocalTime.now();
            LocalTime tokenExpirationTime = now.plusHours(1);

            if (user.getTokenExpirationTime() == null ||
                    now.minusHours(1).isAfter(user.getTokenExpirationTime())) {
                String token = generateRandomToken();
                user.setToken(token);
                user.setTokenExpirationTime(tokenExpirationTime);
                userRepository.save(user);

                return ResponseEntity.ok("You are logged in. Your token is: " + token +
                        ". It will expire in 1 hour.");
            } else {
                return new UnauthorizedException("Login too soon. Please wait before trying again.").toResponseEntity();
            }
        } else {
            return new UnauthorizedException("Invalid username or password").toResponseEntity();
        }
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }
}
