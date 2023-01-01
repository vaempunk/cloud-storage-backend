package com.vaem.cloudstorage.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vaem.cloudstorage.entity.Container;

@Repository
public interface ContainerRepository extends CrudRepository<Container, Integer> {

    public List<Container> findAllByParentId(Integer parentId);

    @Query("SELECT * FROM container WHERE module_id = :moduleId AND parent_id IS NULL")
    public List<Container> findRootByModuleId(@Param("moduleId") int moduleId);

    public boolean existsByNameAndModuleIdAndParentId(String name, int moduleId, Integer parentId);

}
