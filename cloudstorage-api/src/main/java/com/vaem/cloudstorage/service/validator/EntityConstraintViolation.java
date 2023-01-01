package com.vaem.cloudstorage.service.validator;

import com.vaem.cloudstorage.exception.EntityException;

public class EntityConstraintViolation {

    private final EntityException violationException;

    private EntityConstraintViolation(EntityException violationException) {
        this.violationException = violationException;
    }

    public static EntityConstraintViolation fromException(EntityException violationException) {
        return new EntityConstraintViolation(violationException);
    }

    public EntityException getViolationException() {
        return violationException;
    };
    
}
