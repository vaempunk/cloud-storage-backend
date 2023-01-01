package com.vaem.cloudstorage.service.validation;

import java.nio.file.Path;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vaem.cloudstorage.exception.notfound.FolderNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.FolderUniquenessException;
import com.vaem.cloudstorage.repository.FolderRepository;

@Component
public class FolderValidator {

    private final FolderRepository folderRepository;

    public FolderValidator(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Optional<EntityConstraintViolation> validateCreate(Path newFolderPath) {

        EntityConstraintViolation violation = null;

        Path folderParent = newFolderPath.getParent();
        if (folderRepository.elementExists(newFolderPath))
            violation = EntityConstraintViolation.fromException(new FolderUniquenessException());
        else if (!folderRepository.isFolder(folderParent))
            violation = EntityConstraintViolation.fromException(new FolderNotFoundException());

        return Optional.ofNullable(violation);

    }

    public Optional<EntityConstraintViolation> validateMove(Path oldFolderPath, Path newFolderPath) {

        EntityConstraintViolation violation = null;

        if (!folderRepository.isFolder(oldFolderPath) || !folderRepository.isFolder(newFolderPath.getParent()))
            violation = EntityConstraintViolation.fromException(new FolderNotFoundException());
        else if (folderRepository.elementExists(newFolderPath))
            violation = EntityConstraintViolation.fromException(new FolderUniquenessException());

        return Optional.ofNullable(violation);

    }

    public Optional<EntityConstraintViolation> validateDelete(Path folderPath) {

        EntityConstraintViolation violation = null;

        if (!folderRepository.isFolder(folderPath))
            violation = EntityConstraintViolation.fromException(new FolderNotFoundException());

        return Optional.ofNullable(violation);

    }

}
