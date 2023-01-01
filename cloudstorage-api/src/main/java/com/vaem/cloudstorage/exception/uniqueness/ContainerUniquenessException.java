package com.vaem.cloudstorage.exception.uniqueness;

import com.vaem.cloudstorage.exception.EntityException;

public class ContainerUniquenessException extends EntityException {

    public ContainerUniquenessException() {
        super("Container already exists");
    }

}
