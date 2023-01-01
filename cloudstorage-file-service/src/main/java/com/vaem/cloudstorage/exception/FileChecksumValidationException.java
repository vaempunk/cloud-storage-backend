package com.vaem.cloudstorage.exception;

public class FileChecksumValidationException extends EntityException {

    public FileChecksumValidationException() {
        super("File checksum validation failed");
    }

}
