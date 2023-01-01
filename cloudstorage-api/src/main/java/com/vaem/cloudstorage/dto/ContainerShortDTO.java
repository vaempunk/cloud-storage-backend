package com.vaem.cloudstorage.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ContainerShortDTO {

    @NotBlank
    @Length(min = 3, max = 32)
    private String name;

    @Positive
    private int moduleId;

    @Positive
    private Integer parentId;

}
