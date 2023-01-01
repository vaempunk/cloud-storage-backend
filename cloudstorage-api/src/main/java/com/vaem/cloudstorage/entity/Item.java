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
public class Item {

    @Id
    private int id;

    private String name;

    private long size;

    private OffsetDateTime dateCreated;

    private OffsetDateTime dateChanged;

    private int parentId;

}
