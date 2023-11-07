package com.kodilla.ecommercee.domain;


import lombok.Data;

import java.time.LocalTime;


@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Boolean isBlocked;
    private String token;
    private LocalTime loginTime;
}
