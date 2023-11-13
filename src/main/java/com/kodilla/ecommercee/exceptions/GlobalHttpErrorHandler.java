package com.kodilla.ecommercee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Object> handleGroupNotFoundException (GroupNotFoundException exception) {
        return new ResponseEntity<>("Group with given ID doesn't exist.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GroupAlreadyExistsException.class)
    public ResponseEntity<Object> handleGroupAlreadyExistsException (GroupAlreadyExistsException exception) {
        return new ResponseEntity<>("The Group with given name already exists.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(Exception exception) {
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Object> handleProductAlreadyExistsException(Exception exception) {
        return new ResponseEntity<>("Product already exists", HttpStatus.CONFLICT);
    }
}
