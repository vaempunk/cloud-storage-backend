package com.vaem.cloudstorage.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.vaem.cloudstorage.entity.Module;

@Repository
public interface ModuleRepository extends PagingAndSortingRepository<Module, Integer> {

    public List<Module> findAll();

    public boolean existsByName(String name);

    // @Query("SELECT EXISTS (SELECT id FROM module m WHERE m.name = :name AND m.id != :id)")
    // public boolean existsByNameAndIdNot(String name, int id);

}
