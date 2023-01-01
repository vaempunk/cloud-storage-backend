package com.vaem.cloudstorage.exception.notfound;

import com.vaem.cloudstorage.exception.EntityException;

public class FolderNotFoundException extends EntityException {
    
    public FolderNotFoundException() {
        super("Folder not found");
    }

}
