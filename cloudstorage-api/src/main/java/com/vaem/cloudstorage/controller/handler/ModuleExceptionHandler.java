package com.vaem.cloudstorage.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ModuleUniquenessException;

@RestControllerAdvice
public class ModuleExceptionHandler {

    @ExceptionHandler(ModuleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleModuleNotFound(ModuleNotFoundException exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exc.getMessage());
    }

    @ExceptionHandler(ModuleUniquenessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleModuleAlreadyExists(ModuleUniquenessException exc) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exc.getMessage());
    }

}
