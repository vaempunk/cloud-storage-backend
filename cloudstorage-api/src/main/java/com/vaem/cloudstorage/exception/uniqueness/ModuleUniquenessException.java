package com.vaem.cloudstorage.exception.uniqueness;

import com.vaem.cloudstorage.exception.EntityException;

public class ModuleUniquenessException extends EntityException {

    public ModuleUniquenessException() {
        super("Module already exists");
    }

}
