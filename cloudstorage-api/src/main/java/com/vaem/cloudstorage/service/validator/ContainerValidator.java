package com.vaem.cloudstorage.service.validator;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vaem.cloudstorage.entity.Container;
import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ContainerUniquenessException;
import com.vaem.cloudstorage.repository.ContainerRepository;
import com.vaem.cloudstorage.repository.ModuleRepository;

@Component
public class ContainerValidator {

    private final ContainerRepository containerRepository;
    private final ModuleRepository moduleRepository;

    public ContainerValidator(
            ContainerRepository containerRepository,
            ModuleRepository moduleRepository) {

        this.containerRepository = containerRepository;
        this.moduleRepository = moduleRepository;
    }

    public Optional<EntityConstraintViolation> validateAdd(Container container) {

        EntityConstraintViolation violation = null;

        if (containerRepository.existsByNameAndModuleIdAndParentId(
                container.getName(),
                container.getModuleId(),
                container.getParentId())) {
            violation = EntityConstraintViolation.fromException(new ContainerUniquenessException());
        }

        else if (container.getParentId() != null && !containerRepository.existsById(container.getParentId()))
            violation = EntityConstraintViolation
                    .fromException(new ContainerNotFoundException(container.getParentId()));

        else if (!moduleRepository.existsById(container.getModuleId()))
            violation = EntityConstraintViolation
                    .fromException(new ModuleNotFoundException(container.getModuleId()));

        return Optional.ofNullable(violation);

    }

    public Optional<EntityConstraintViolation> validateUpdate(Container newContainer, Container oldContainer) {

        EntityConstraintViolation violation = null;

        boolean nameUpdated = !newContainer.getName().equals(oldContainer.getName());
        boolean moduleIdUpdated = newContainer.getModuleId() != oldContainer.getModuleId();
        boolean parentIdUpdated = !Objects.equals(newContainer.getParentId(), oldContainer.getParentId());
        boolean nameAndMoudleIdAndParentIdNotUnique = containerRepository.existsByNameAndModuleIdAndParentId(
                newContainer.getName(), newContainer.getModuleId(), newContainer.getParentId());

        if ((nameUpdated || moduleIdUpdated || parentIdUpdated) && nameAndMoudleIdAndParentIdNotUnique)
            violation = EntityConstraintViolation
                    .fromException(new ContainerUniquenessException());

        else if (moduleIdUpdated && !moduleRepository.existsById(newContainer.getModuleId()))
            violation = EntityConstraintViolation
                    .fromException(new ModuleNotFoundException(newContainer.getModuleId()));

        else if (parentIdUpdated && !containerRepository.existsById(newContainer.getParentId()))
            violation = EntityConstraintViolation
                    .fromException(new ContainerNotFoundException(newContainer.getParentId()));

        return Optional.ofNullable(violation);

    }

    public Optional<EntityConstraintViolation> validateDelete(int containerId) {

        EntityConstraintViolation violation = null;
        if (!containerRepository.existsById(containerId))
            violation = EntityConstraintViolation.fromException(new ContainerNotFoundException(containerId));

        return Optional.ofNullable(violation);

    }

}
