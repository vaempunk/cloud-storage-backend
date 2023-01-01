package com.vaem.cloudstorage.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ModuleShortDTO {

    @NotBlank
    @Length(min = 3, max = 32)
    private String name;

    @NotNull
    @Length(min = 0, max = 128)
    private String description;

}
