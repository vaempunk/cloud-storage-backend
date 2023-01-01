package com.vaem.cloudstorage.exception.notfound;

import com.vaem.cloudstorage.exception.EntityException;

public class ContainerNotFoundException extends EntityException {

    private final int containerId;

    public ContainerNotFoundException(int containerId) {
        super("Container not found");
        this.containerId = containerId;
    }

    public int getContainerId() {
        return containerId;
    }

}
