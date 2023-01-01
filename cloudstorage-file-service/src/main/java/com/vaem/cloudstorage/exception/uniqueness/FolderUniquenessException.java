package com.vaem.cloudstorage.exception.uniqueness;

import com.vaem.cloudstorage.exception.EntityException;

public class FolderUniquenessException extends EntityException {
    
    public FolderUniquenessException() {
        super("Folder is not unique");
    }

}   
