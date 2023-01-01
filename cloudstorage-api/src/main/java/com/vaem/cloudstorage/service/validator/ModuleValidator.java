package com.vaem.cloudstorage.service.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vaem.cloudstorage.entity.Module;
import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ModuleUniquenessException;
import com.vaem.cloudstorage.repository.ModuleRepository;

@Component
public class ModuleValidator {

    private final ModuleRepository moduleRepository;

    public ModuleValidator(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Optional<EntityConstraintViolation> validateAdd(Module module) {

        EntityConstraintViolation violation = null;
        if (moduleRepository.existsByName(module.getName()))
            violation = EntityConstraintViolation.fromException(new ModuleUniquenessException());

        return Optional.ofNullable(violation);
    }

    public Optional<EntityConstraintViolation> validateUpdate(Module newModule, Module oldModule) {

        EntityConstraintViolation violation = null;
        if (!oldModule.getName().equals(newModule.getName())
                && moduleRepository.existsByName(newModule.getName()))
            violation = EntityConstraintViolation.fromException(new ModuleUniquenessException());

        return Optional.ofNullable(violation);

    }

    public Optional<EntityConstraintViolation> validateDelete(int moduleId) {

        EntityConstraintViolation violation = null;
        if (!moduleRepository.existsById(moduleId))
            violation = EntityConstraintViolation.fromException(new ModuleNotFoundException(moduleId));

        return Optional.ofNullable(violation);
    }

}
