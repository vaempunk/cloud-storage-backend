package com.vaem.cloudstorage.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.repository.FolderRepository;
import com.vaem.cloudstorage.service.validation.EntityConstraintViolation;
import com.vaem.cloudstorage.service.validation.FolderValidator;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderValidator folderValidator;

    public FolderService(
            FolderRepository folderRepository,
            FolderValidator folderValidator) {
        this.folderRepository = folderRepository;
        this.folderValidator = folderValidator;
    }

    // public List<Path> getFoldersByParent(Path parentRelPath) throws IOException {
    //     Path parentPath = storagePath.resolve(parentRelPath);
    //     return Files
    //             .walk(parentPath, 1)
    //             .filter(path -> Files.isDirectory(path))
    //             .toList();
    // }

    public void createFolder(Path folderPath) throws IOException, EntityException {

        Optional<EntityConstraintViolation> violation = folderValidator.validateCreate(folderPath);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        folderRepository.create(folderPath);

    }

    public void moveFolder(Path oldFolderPath, Path newFolderPath)
            throws IOException, EntityException {

        Optional<EntityConstraintViolation> violation = folderValidator
                .validateMove(oldFolderPath, newFolderPath);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        folderRepository.move(oldFolderPath, newFolderPath);

    }

    public void deleteFolder(Path folderPath) throws IOException, EntityException {

        Optional<EntityConstraintViolation> violation = folderValidator.validateDelete(folderPath);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        folderRepository.delete(folderPath);

    }

}
