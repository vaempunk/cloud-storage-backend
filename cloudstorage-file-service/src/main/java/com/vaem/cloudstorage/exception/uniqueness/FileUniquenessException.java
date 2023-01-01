package com.vaem.cloudstorage.exception.uniqueness;

import com.vaem.cloudstorage.exception.EntityException;

public class FileUniquenessException extends EntityException {

    public FileUniquenessException() {
        super("File is not unique");
    }
    
}
