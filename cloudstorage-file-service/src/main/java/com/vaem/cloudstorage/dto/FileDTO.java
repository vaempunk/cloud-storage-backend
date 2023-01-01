package com.vaem.cloudstorage.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileDTO(
        String parentPath,
        MultipartFile file) {

}
