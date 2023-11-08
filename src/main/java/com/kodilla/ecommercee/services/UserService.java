package com.kodilla.ecommercee.services;

import com.kodilla.ecommercee.domain.User;
import com.kodilla.ecommercee.domain.UserDto;
import com.kodilla.ecommercee.exception.UnauthorizedException;
import com.kodilla.ecommercee.exception.UserNotFoundException;
import com.kodilla.ecommercee.mapper.UserMapper;
import com.kodilla.ecommercee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
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
    public String createActiveSession(UserDto loginData) {
        User user = userRepository.findByUsername(loginData.getUsername());

        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            LocalTime now = LocalTime.now();
            LocalTime lastLoginTime = user.getLoginTime();

            if (lastLoginTime == null || now.minusHours(1).isAfter(lastLoginTime)) {
                String token = generateRandomToken();
                user.setToken(token);
                user.setLoginTime(now);
                userRepository.save(user);
                scheduleSessionExpiration(user.getId());
                return "You are logged in. Your token is: " + token + " It will expire in 1 hour.";
            } else {
                throw new UnauthorizedException("Login too soon. Please wait before trying again.");
            }
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
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
}
