package com.vaem.cloudstorage.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ContainerUniquenessException;

@RestControllerAdvice
public class ContainerExceptionHandler {

    @ExceptionHandler(ContainerNotFoundException.class)
    public ResponseEntity<Object> handleContainerNotFound(ContainerNotFoundException exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exc.getMessage());
    }

    @ExceptionHandler(ContainerUniquenessException.class)
    public ResponseEntity<Object> handleContainerAlreadyExists(ContainerUniquenessException exc) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exc.getMessage());
    }

}
