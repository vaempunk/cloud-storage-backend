package com.vaem.cloudstorage.controller;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vaem.cloudstorage.entity.File;
import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{*pathStr}")
    public ResponseEntity<File> getFile(@PathVariable("pathStr") String pathStr)
            throws IOException, EntityException {
        Path filePath = Path.of(pathStr);
        File res = fileService.getFile(filePath);
        String filename = res.getPath().getFileName().toString();
        return ResponseEntity
                .accepted()
                .header("Content-Disposition", String.format("attchment; filename=\"%s\"", filename))
                .body(res);

    }

    @PostMapping("/{*parentPathStr}")
    public void postFile(
            @PathVariable("parentPathStr") String parentPathStr,
            @RequestBody MultipartFile file,
            @RequestHeader("Digest") String checksum) throws IOException, EntityException {

        File fileEntity = new File();
        fileEntity.setPath(Path.of(parentPathStr, file.getOriginalFilename()));
        fileEntity.setContent(file.getBytes());

        fileService.saveFile(fileEntity, checksum);

    }

    @PutMapping("/{*oldPathStr}")
    public void moveFile(
            @PathVariable("oldPathStr") String oldPathStr,
            @RequestBody String newPathStr) throws IOException, EntityException {

        Path oldFilePath = Path.of(oldPathStr);
        Path newFilePath = Path.of(newPathStr);

        fileService.moveFile(oldFilePath, newFilePath);

    }

    @DeleteMapping("/{*pathStr}")
    public void deleteFile(@PathVariable("pathStr") String pathStr)
            throws IOException, EntityException {
        Path filePath = Path.of(pathStr);

        fileService.deleteFile(filePath);
    }

}
