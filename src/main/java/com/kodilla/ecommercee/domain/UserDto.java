package com.kodilla.ecommercee.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private Boolean isBlocked;
    private Long token;
}
