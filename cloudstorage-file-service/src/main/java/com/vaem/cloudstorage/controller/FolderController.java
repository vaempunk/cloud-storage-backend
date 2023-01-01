package com.vaem.cloudstorage.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.service.FolderService;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/{*pathStr}")
    public void postFolder(@PathVariable String pathStr)
            throws IOException, EntityException {

        Path folderPath = Path.of(pathStr);
        folderService.createFolder(folderPath);

    }

    @PutMapping("/{*oldPathStr}")
    public void moveFolder(
            @PathVariable String oldPathStr,
            @RequestBody String newPathStr) throws IOException, EntityException {

        Path oldFolderPath = Path.of(oldPathStr);
        Path newFolderPath = Path.of(newPathStr);
        folderService.moveFolder(oldFolderPath, newFolderPath);

    }

    @DeleteMapping("/{*pathStr}")
    public void deleteFolder(@PathVariable String pathStr)
            throws IOException, EntityException {

        Path folderPath = Path.of(pathStr);
        folderService.deleteFolder(folderPath);

    }

}
