package com.vaem.cloudstorage.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vaem.cloudstorage.entity.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {

    public List<Item> findAllByParentId(int parentId);

    public boolean existsByNameAndParentId(String name, int parentId);

}
