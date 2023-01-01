package com.vaem.cloudstorage.controller.handler;

import java.io.IOException;
import java.nio.file.InvalidPathException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaem.cloudstorage.exception.EntityException;

@RestControllerAdvice
public class FileSystemExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException exc) {
        return ResponseEntity
                .internalServerError()
                .body("Can't process operation.");
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<?> handleInvalidPathException(InvalidPathException exc) {
        return ResponseEntity
                .badRequest()
                .body(String.format("Path is invalid.\nInput:\t%s.\nReason:\t%s", exc.getInput(), exc.getReason()));
    }

    @ExceptionHandler(EntityException.class)
    public ResponseEntity<?> handleEntityException(EntityException exc) {
        return ResponseEntity
                .badRequest()
                .body(exc.getMessage());
    }

}
