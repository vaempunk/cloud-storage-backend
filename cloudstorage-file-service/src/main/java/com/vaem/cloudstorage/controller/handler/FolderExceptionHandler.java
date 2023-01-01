package com.vaem.cloudstorage.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaem.cloudstorage.exception.notfound.FolderNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.FolderUniquenessException;

@RestControllerAdvice
public class FolderExceptionHandler {

    @ExceptionHandler(FolderNotFoundException.class)
    public ResponseEntity<?> handleFolderNotFoundException(FolderNotFoundException exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exc.getMessage());
    }

    @ExceptionHandler(FolderUniquenessException.class)
    public ResponseEntity<?> handleFolderUniquenessException(FolderUniquenessException exc) {
        return ResponseEntity
                .badRequest()
                .body(exc.getMessage());
    }

}
