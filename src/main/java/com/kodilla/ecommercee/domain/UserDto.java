package com.kodilla.ecommercee.domain;


import lombok.Data;


@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Boolean isBlocked;
    private Long token;
}
