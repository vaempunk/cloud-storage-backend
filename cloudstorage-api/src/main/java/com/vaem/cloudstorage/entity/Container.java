package com.vaem.cloudstorage.entity;

import java.time.OffsetDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Container {

    @Id
    private int id;

    private String name;

    private OffsetDateTime dateCreated;

    private int moduleId;

    private Integer parentId;

}
