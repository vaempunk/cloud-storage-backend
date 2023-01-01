package com.vaem.cloudstorage.service.validation;

import java.nio.file.Path;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import com.vaem.cloudstorage.exception.notfound.FileNotFoundException;
import com.vaem.cloudstorage.exception.notfound.FolderNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.FileUniquenessException;
import com.vaem.cloudstorage.repository.FileRepository;

@Component
public class FileValidator {

    private final FileRepository fileRepository;

    public FileValidator(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Optional<EntityConstraintViolation> validateSave(Path newFilePath) {

        EntityConstraintViolation violation = null;

        Path fileParent = newFilePath.getParent();
        if (fileRepository.elementExists(newFilePath))
            violation = EntityConstraintViolation.fromException(new FileUniquenessException());
        else if (!fileRepository.isFolder(fileParent))
            violation = EntityConstraintViolation.fromException(new FolderNotFoundException());

        return Optional.ofNullable(violation);

    }

    public boolean checksumIsValid(byte[] fileBytes, String excpectedCheckSum) {
        String realChecksum = "sha256=" + DigestUtils.sha256Hex(fileBytes);

        return realChecksum.equalsIgnoreCase(excpectedCheckSum);
    }

    public Optional<EntityConstraintViolation> validateMove(Path filePath, Path fileNewPath) {

        EntityConstraintViolation violation = null;

        if (!fileRepository.isFile(filePath))
            violation = EntityConstraintViolation.fromException(new FileNotFoundException());
        else if (!fileRepository.isFolder(fileNewPath.getParent()))
            violation = EntityConstraintViolation.fromException(new FolderNotFoundException());
        else if (fileRepository.elementExists(fileNewPath))
            violation = EntityConstraintViolation.fromException(new FileUniquenessException());

        return Optional.ofNullable(violation);

    }

    public Optional<EntityConstraintViolation> validateDelete(Path filePath) {

        EntityConstraintViolation violation = null;

        if (!fileRepository.isFile(filePath))
            violation = EntityConstraintViolation.fromException(new FileNotFoundException());

        return Optional.ofNullable(violation);

    }

}
