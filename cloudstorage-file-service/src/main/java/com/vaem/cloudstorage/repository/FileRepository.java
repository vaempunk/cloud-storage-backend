package com.vaem.cloudstorage.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.vaem.cloudstorage.entity.File;

@Repository
public class FileRepository {

    private final Path storagePath;
    private final Path ROOT = Path.of("/");

    public FileRepository(
            @Value("${cloudstorage.filemanager.storagepath}") String storagePathString) {
        this.storagePath = Path.of(storagePathString);
    }

    public Optional<File> findByPath(Path filePath) throws IOException {

        if (!isFile(filePath))
            return Optional.empty();

        Path fileAbsolutePath = resolvePath(filePath);

        File file = new File();
        file.setPath(fileAbsolutePath);
        file.setContent(Files.readAllBytes(fileAbsolutePath));
        return Optional.of(file);

    }

    public void save(File file) throws IOException {
        Path filePath = resolvePath(file.getPath());
        Files.createFile(filePath);
        Files.write(filePath, file.getContent());
    }

    public void move(Path oldFilePath, Path newFilePath) throws IOException {
        Path oldFileAbsolutePath = resolvePath(oldFilePath);
        Path newFileAbsolutePath = resolvePath(newFilePath);
        Files.move(oldFileAbsolutePath, newFileAbsolutePath);
    }

    public void deleteByPath(Path fileRelPath) throws IOException {
        Path filePath = resolvePath(fileRelPath);
        Files.delete(filePath);
    }

    public boolean elementExists(Path elementRelPath) {
        Path elementPath = resolvePath(elementRelPath);
        return Files.exists(elementPath);
    }

    public boolean isFile(Path fileRelPath) {
        Path filePath = resolvePath(fileRelPath);
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }

    public boolean isFolder(Path folderRelPath) {
        Path folderPath = resolvePath(folderRelPath);
        return Files.exists(folderPath) && Files.isDirectory(folderPath);
    }

    private Path resolvePath(Path path) {
        Path relative = ROOT.relativize(path);
        return storagePath.resolve(relative);
    }

}
