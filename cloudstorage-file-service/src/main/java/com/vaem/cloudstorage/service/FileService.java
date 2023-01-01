package com.vaem.cloudstorage.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vaem.cloudstorage.entity.File;
import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.exception.FileChecksumValidationException;
import com.vaem.cloudstorage.exception.notfound.FileNotFoundException;
import com.vaem.cloudstorage.repository.FileRepository;
import com.vaem.cloudstorage.service.validation.EntityConstraintViolation;
import com.vaem.cloudstorage.service.validation.FileValidator;

@Service
public class FileService {

    private final FileRepository fileRepository;

    private final FileValidator fileValidator;

    public FileService(
            FileRepository fileRepository,
            FileValidator fileValidator) {
        this.fileRepository = fileRepository;
        this.fileValidator = fileValidator;
    }

    public File getFile(Path filePath) throws IOException, EntityException {
        return fileRepository.findByPath(filePath)
                .orElseThrow(() -> new FileNotFoundException());
    }

    public void saveFile(
            File file,
            String checksum)
            throws IOException, EntityException {

        Optional<EntityConstraintViolation> violation = fileValidator.validateSave(file.getPath());
        if (violation.isPresent())
            throw violation.get().getViolationException();
        if (!fileValidator.checksumIsValid(file.getContent(), checksum))
            throw new FileChecksumValidationException();

        fileRepository.save(file);
    }

    public void moveFile(Path oldFilePath, Path newFilePath) throws IOException, EntityException {

        Optional<EntityConstraintViolation> violation = fileValidator.validateMove(oldFilePath, newFilePath);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        fileRepository.move(oldFilePath, newFilePath);

    }

    public void deleteFile(Path filePath) throws IOException, EntityException {

        Optional<EntityConstraintViolation> violation = fileValidator.validateDelete(filePath);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        fileRepository.deleteByPath(filePath);

    }

}
