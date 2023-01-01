package com.vaem.cloudstorage.exception.uniqueness;

import com.vaem.cloudstorage.exception.EntityException;

public class ItemUniquenessException extends EntityException {

    public ItemUniquenessException() {
        super("Item already exists");
    }

}
