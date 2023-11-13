package com.kodilla.ecommercee.mapper;

import com.kodilla.ecommercee.domain.User;
import com.kodilla.ecommercee.domain.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setIsBlocked(user.getIsBlocked());
        userDto.setToken(user.getToken());
        userDto.setTokenExpirationTime(user.getTokenExpirationTime());
        return userDto;
    }

    public User mapToEntityUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setIsBlocked(userDto.getIsBlocked());
        user.setToken(userDto.getToken());
        user.setTokenExpirationTime(userDto.getTokenExpirationTime());
        return user;
    }
}

