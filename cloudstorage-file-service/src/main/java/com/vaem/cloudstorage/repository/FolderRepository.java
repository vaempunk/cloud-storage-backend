package com.vaem.cloudstorage.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FolderRepository {

    private final Path storagePath;
    private final Path ROOT = Path.of("/");

    public FolderRepository(
            @Value("${cloudstorage.filemanager.storagepath}") String storagePathString) {
        this.storagePath = Path.of(storagePathString);
    }

    public void create(Path folderPath) throws IOException {
        Path folderAbsolutePath = resolvePath(folderPath);
        Files.createDirectory(folderAbsolutePath);
    }

    public void move(Path oldFolderPath, Path newFolderPath)
            throws IOException {
        Path oldFolderAbsolutePath = resolvePath(oldFolderPath);
        Path newFolderAbsolutePath = resolvePath(newFolderPath);
        Files.move(oldFolderAbsolutePath, newFolderAbsolutePath);
    }

    public void delete(Path folderPath) throws IOException {
        Path folderAbsolutePath = resolvePath(folderPath);
        Files.delete(folderAbsolutePath);
    }

    public boolean elementExists(Path elementPath) {
        Path elementAbsolutePath = resolvePath(elementPath);
        return Files.exists(elementAbsolutePath);
    }

    public boolean isFolder(Path folderPath) {
        Path folderAbsolutePath = resolvePath(folderPath);
        return Files.exists(folderAbsolutePath) && Files.isDirectory(folderAbsolutePath);
    }

    private Path resolvePath(Path path) {
        Path relative = ROOT.relativize(path);
        return storagePath.resolve(relative);
    }

}
