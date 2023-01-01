package com.vaem.cloudstorage.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vaem.cloudstorage.entity.Container;
import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.repository.ContainerRepository;
import com.vaem.cloudstorage.service.validator.ContainerValidator;
import com.vaem.cloudstorage.service.validator.EntityConstraintViolation;

@Service
public class ContainerService {

    private final ContainerRepository containerRepository;
    private final ContainerValidator containerValidator;

    public ContainerService(
            ContainerRepository containerRepository,
            ContainerValidator containerValidator) {
        this.containerRepository = containerRepository;
        this.containerValidator = containerValidator;
    }

    public Container getById(int containerId) throws ContainerNotFoundException {

        Container container = containerRepository
                .findById(containerId)
                .orElseThrow(() -> new ContainerNotFoundException(containerId));

        return container;

    }

    public List<Container> getAllByParentId(Integer parentId) {
        return containerRepository.findAllByParentId(parentId);
    }

    public List<Container> getAllRootContainers(int moduleId) {
        return containerRepository.findRootByModuleId(moduleId);
    }

    public int add(Container container) throws EntityException {

        Optional<EntityConstraintViolation> violationOpt = containerValidator.validateAdd(container);
        if (violationOpt.isPresent())
            throw violationOpt.get().getViolationException();

        container.setDateCreated(OffsetDateTime.now());

        containerRepository.save(container);
        return container.getId();

    }

    public void update(Container newContainer) throws EntityException {

        Container oldContainer = containerRepository
                .findById(newContainer.getId())
                .orElseThrow(() -> new ContainerNotFoundException(newContainer.getId()));

        Optional<EntityConstraintViolation> violationOpt = containerValidator.validateUpdate(
                newContainer, oldContainer);
        if (violationOpt.isPresent())
            throw violationOpt.get().getViolationException();

        oldContainer.setName(newContainer.getName());
        oldContainer.setModuleId(newContainer.getModuleId());
        oldContainer.setParentId(newContainer.getParentId());

        containerRepository.save(oldContainer);

    }

    public void deleteById(int containerId) throws EntityException {

        Optional<EntityConstraintViolation> violationOpt = containerValidator.validateDelete(containerId);
        if (violationOpt.isPresent())
            throw violationOpt.get().getViolationException();

        containerRepository.deleteById(containerId);

    }

    public String getPath(int containerId) throws ContainerNotFoundException {

        Container container = getById(containerId);
        StringBuilder pathSB = new StringBuilder(String.format("/%d", container.getModuleId()));
        pathSB.append(String.format("/%s", container.getName()));

        while (container.getParentId() != 0) {
            container = getById(containerId);
            pathSB.append(String.format("/%s", container.getName()));
        }

        return pathSB.toString();

    }

}
