package com.vaem.cloudstorage.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vaem.cloudstorage.dto.ContainerShortDTO;
import com.vaem.cloudstorage.entity.Container;
import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.service.ContainerService;

@RestController
@RequestMapping
public class ContainerController {

    private final ContainerService containerService;
    private final ModelMapper modelMapper;

    public ContainerController(ContainerService containerService, ModelMapper modelMapper) {
        this.containerService = containerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/containers/{id}")
    public Container getContainer(@PathVariable int id) throws ContainerNotFoundException {
        return containerService.getById(id);
    }

    @GetMapping("/containers/{parentId}")
    public List<Container> getAllContainersByParentId(@PathVariable(required = false) Integer parentId) {
        return containerService.getAllByParentId(parentId);
    }

    @GetMapping("/modules/{moduleId}/containers")
    public List<Container> getAllRootContainers(@PathVariable("moduleId") int moduleId) {
        return containerService.getAllRootContainers(moduleId);
    }

    @PostMapping("/containers")
    public ResponseEntity<?> postContainer(@RequestBody @Valid ContainerShortDTO containerRequestDTO)
            throws EntityException {

        Container container = modelMapper.map(containerRequestDTO, Container.class);
        int containerId = containerService.add(container);

        return ResponseEntity
                .created(URI.create(String.format("/containers/%d", containerId)))
                .build();

    }

    @PutMapping("/containers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putContainer(
            @RequestBody @Valid ContainerShortDTO containerRequestDTO,
            @PathVariable int id)
            throws EntityException {

        Container container = modelMapper.map(containerRequestDTO, Container.class);
        container.setId(id);
        containerService.update(container);

    }

    @DeleteMapping("/containers/{id}")
    public void deleteContainer(@PathVariable int id) throws EntityException {
        containerService.deleteById(id);
    }

    @GetMapping("/containers/{id}/path")
    public String getContainerPath(@PathVariable int id) throws EntityException {
        return containerService.getPath(id);
    }

}
