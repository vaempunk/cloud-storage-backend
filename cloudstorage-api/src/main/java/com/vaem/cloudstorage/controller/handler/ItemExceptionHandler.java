package com.vaem.cloudstorage.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaem.cloudstorage.exception.notfound.ItemNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ItemUniquenessException;

@RestControllerAdvice
public class ItemExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> handleItemNotFound(ItemNotFoundException exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exc.getMessage());
    }

    @ExceptionHandler(ItemUniquenessException.class)
    public ResponseEntity<Object> handleItemAlreadyExists(ItemUniquenessException exc) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exc.getMessage());
    }

}
