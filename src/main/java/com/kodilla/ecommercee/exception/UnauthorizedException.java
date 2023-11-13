package com.kodilla.ecommercee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public ResponseEntity<String> toResponseEntity() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(getMessage());
    }

    public ResponseEntity<String> toResponseEntity(Throwable cause) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(getMessage());
    }
}
