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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vaem.cloudstorage.dto.ItemShortDTO;
import com.vaem.cloudstorage.entity.Item;
import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.exception.notfound.ItemNotFoundException;
import com.vaem.cloudstorage.service.ItemService;

@RestController
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper;

    public ItemController(ItemService itemService, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable int id) throws ItemNotFoundException {
        return itemService.getById(id);
    }

    @GetMapping("/containers/{parentId}/items")
    public List<Item> getAllItemsByParentId(@PathVariable int parentId) {
        return itemService.getAllByParentId(parentId);
    }

    @PostMapping("/items")
    public ResponseEntity<?> postItem(@RequestBody @Valid ItemShortDTO itemRequestDTO)
            throws EntityException {

        Item item = modelMapper.map(itemRequestDTO, Item.class);
        int id = itemService.add(item);

        return ResponseEntity
                .created(URI.create(String.format("/items/%d", id)))
                .build();

    }

    @PutMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putItem(
            @RequestBody @Valid ItemShortDTO itemRequestDTO,
            @PathVariable int id)
            throws EntityException {

        Item item = modelMapper.map(itemRequestDTO, Item.class);
        item.setId(id);

        itemService.update(item);

    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable int id) throws EntityException {
        itemService.deleteById(id);
    }

    @GetMapping("/items/{id}/path")
    public String getItemPath(@PathVariable int id) throws ItemNotFoundException {
        return itemService.getPath(id);
    }

}
