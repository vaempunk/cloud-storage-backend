package com.vaem.cloudstorage.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vaem.cloudstorage.entity.Item;
import com.vaem.cloudstorage.exception.EntityException;
import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.exception.notfound.ItemNotFoundException;
import com.vaem.cloudstorage.repository.ItemRepository;
import com.vaem.cloudstorage.service.validator.EntityConstraintViolation;
import com.vaem.cloudstorage.service.validator.ItemValidator;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    private final ContainerService containerService;

    public ItemService(
            ItemRepository itemRepository,
            ItemValidator itemValidator,
            ContainerService containerService) {
        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
        this.containerService = containerService;
    }

    public Item getById(int itemId) throws ItemNotFoundException {

        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));

        return item;

    }

    public List<Item> getAllByParentId(int parentId) {

        return itemRepository.findAllByParentId(parentId);

    }

    public int add(Item item) throws EntityException {

        Optional<EntityConstraintViolation> violation = itemValidator.validateAdd(item);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        item.setDateCreated(OffsetDateTime.now());
        item.setDateChanged(OffsetDateTime.now());

        itemRepository.save(item);
        return item.getId();

    }

    public void update(Item newItem) throws EntityException {

        Item oldItem = itemRepository
                .findById(newItem.getId())
                .orElseThrow(() -> new ItemNotFoundException(newItem.getId()));

        Optional<EntityConstraintViolation> violation = itemValidator.validateUpdate(newItem, oldItem);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        oldItem.setName(newItem.getName());
        oldItem.setSize(newItem.getSize());
        oldItem.setDateChanged(OffsetDateTime.now());
        oldItem.setParentId(newItem.getParentId());

        itemRepository.save(oldItem);

    }

    public void deleteById(int itemId) throws EntityException {

        Optional<EntityConstraintViolation> violation = itemValidator.validateDelete(itemId);
        if (violation.isPresent())
            throw violation.get().getViolationException();

        itemRepository.deleteById(itemId);

    }

    public String getPath(int itemId) throws ItemNotFoundException {

        Item item = getById(itemId);

        String motherPath = null;
        try {
            motherPath = containerService.getPath(item.getParentId());
        } catch (ContainerNotFoundException exc) {
            throw new RuntimeException("Undefined parent of item: " + item.getId(), exc);
        }

        return String.format("%s/%d", motherPath, item.getId());

    }

}
