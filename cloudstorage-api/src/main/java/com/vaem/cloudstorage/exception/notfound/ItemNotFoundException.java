package com.vaem.cloudstorage.exception.notfound;

import com.vaem.cloudstorage.exception.EntityException;

public class ItemNotFoundException extends EntityException {

    private final int itemId;

    public ItemNotFoundException(int itemId) {
        super("Item not found");
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

}
