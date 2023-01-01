package com.vaem.cloudstorage.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaem.cloudstorage.exception.notfound.FileNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.FileUniquenessException;

@RestControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exc.getMessage());
    }

    @ExceptionHandler(FileUniquenessException.class)
    public ResponseEntity<?> handleFileUniquenessException(FileUniquenessException exc) {
        return ResponseEntity
                .badRequest()
                .body(exc.getMessage());
    }

}
