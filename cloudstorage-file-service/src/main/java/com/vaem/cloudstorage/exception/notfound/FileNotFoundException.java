package com.vaem.cloudstorage.exception.notfound;

import com.vaem.cloudstorage.exception.EntityException;

public class FileNotFoundException extends EntityException {
    
    public FileNotFoundException() {
        super("File not found");
    }

}
