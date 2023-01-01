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

import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;
import com.vaem.cloudstorage.dto.ModuleShortDTO;
import com.vaem.cloudstorage.entity.Module;
import com.vaem.cloudstorage.service.ModuleService;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModelMapper modelMapper;

    public ModuleController(ModuleService moduleService, ModelMapper modelMapper) {
        this.moduleService = moduleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public Module getModule(@PathVariable int id) throws ModuleNotFoundException {
        return moduleService.getById(id);
    }

    @GetMapping
    public List<Module> getAllModules() {
        return moduleService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> postModule(@RequestBody @Valid ModuleShortDTO moduleRequestDTO)
            throws EntityException {

        Module module = modelMapper.map(moduleRequestDTO, Module.class);
        int id = moduleService.add(module);

        return ResponseEntity
                .created(URI.create(String.format("/modules/%d", id)))
                .build();

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putModule(
            @RequestBody @Valid ModuleShortDTO moduleRequestDTO,
            @PathVariable int id)
            throws EntityException {

        Module module = modelMapper.map(moduleRequestDTO, Module.class);
        module.setId(id);

        moduleService.update(module);

    }

    @DeleteMapping("/{id}")
    public void deleteModule(@PathVariable int id) throws EntityException {
        moduleService.deleteById(id);
    }

}
