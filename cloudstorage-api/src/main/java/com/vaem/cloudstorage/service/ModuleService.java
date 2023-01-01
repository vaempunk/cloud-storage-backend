package com.vaem.cloudstorage.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vaem.cloudstorage.repository.ModuleRepository;
import com.vaem.cloudstorage.service.validator.EntityConstraintViolation;
import com.vaem.cloudstorage.service.validator.ModuleValidator;
import com.vaem.cloudstorage.entity.Module;
import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleValidator moduleValidator;

    public ModuleService(ModuleRepository moduleRepository, ModuleValidator moduleValidator) {
        this.moduleRepository = moduleRepository;
        this.moduleValidator = moduleValidator;
    }

    public List<Module> getAll() {
        return moduleRepository.findAll();
    }

    public Module getById(int moduleId) throws ModuleNotFoundException {

        Module module = moduleRepository
                .findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        return module;

    }

    public int add(Module module) throws EntityException {

        Optional<EntityConstraintViolation> violation = moduleValidator.validateAdd(module);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        module.setDateCreated(OffsetDateTime.now());

        moduleRepository.save(module);
        return module.getId();

    }

    public void update(Module newModule) throws EntityException {

        Module oldModule = moduleRepository
                .findById(newModule.getId())
                .orElseThrow(() -> new ModuleNotFoundException(newModule.getId()));

        Optional<EntityConstraintViolation> violation = moduleValidator.validateUpdate(newModule, oldModule);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        oldModule.setName(newModule.getName());
        oldModule.setDescription(newModule.getDescription());

        moduleRepository.save(oldModule);

    }

    public void deleteById(int moduleId) throws EntityException {

        Optional<EntityConstraintViolation> violation = moduleValidator.validateDelete(moduleId);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        moduleRepository.deleteById(moduleId);

    }

}
