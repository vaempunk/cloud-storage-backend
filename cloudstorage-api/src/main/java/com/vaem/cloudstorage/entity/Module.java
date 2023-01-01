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
public class Module {

    @Id
    private int id;

    private String name;

    private String description;

    private OffsetDateTime dateCreated;

}
