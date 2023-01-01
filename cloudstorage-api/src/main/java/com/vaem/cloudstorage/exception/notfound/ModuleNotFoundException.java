package com.vaem.cloudstorage.exception.notfound;

import com.vaem.cloudstorage.exception.EntityException;

public class ModuleNotFoundException extends EntityException {

    private final int moduleId;

    public ModuleNotFoundException(int moduleId) {
        super("Module not found");
        this.moduleId = moduleId;
    }

    public int getModuleId() {
        return moduleId;
    }

}
