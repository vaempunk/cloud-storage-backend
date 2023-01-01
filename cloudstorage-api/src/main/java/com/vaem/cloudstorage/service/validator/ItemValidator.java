package com.vaem.cloudstorage.service.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vaem.cloudstorage.entity.Item;
import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.exception.notfound.ItemNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ItemUniquenessException;
import com.vaem.cloudstorage.repository.ContainerRepository;
import com.vaem.cloudstorage.repository.ItemRepository;

@Component
public class ItemValidator {

    private final ItemRepository itemRepository;
    private final ContainerRepository containerRepository;

    public ItemValidator(
            ItemRepository itemRepository,
            ContainerRepository containerRepository) {

        this.itemRepository = itemRepository;
        this.containerRepository = containerRepository;
    }

    public Optional<EntityConstraintViolation> validateAdd(Item item) {

        EntityConstraintViolation violation = null;

        if (itemRepository.existsByNameAndParentId(item.getName(), item.getParentId()))
            violation = EntityConstraintViolation.fromException(new ItemUniquenessException());

        else if (!containerRepository.existsById(item.getParentId()))
            violation = EntityConstraintViolation.fromException(
                    new ContainerNotFoundException(item.getParentId()));

        return Optional.ofNullable(violation);
    }

    public Optional<EntityConstraintViolation> validateUpdate(Item newItem, Item oldItem) {

        EntityConstraintViolation violation = null;

        boolean nameUpdated = !newItem.getName().equals(oldItem.getName());
        boolean parentIdUpdated = newItem.getParentId() != oldItem.getParentId();
        boolean nameAndParentIdNotUnique = itemRepository
                .existsByNameAndParentId(newItem.getName(), newItem.getParentId());

        if ((nameUpdated || parentIdUpdated) && nameAndParentIdNotUnique)
            violation = EntityConstraintViolation.fromException(
                    new ItemUniquenessException());

        else if (parentIdUpdated && !containerRepository.existsById(newItem.getParentId()))
            violation = EntityConstraintViolation.fromException(
                    new ContainerNotFoundException(newItem.getParentId()));

        return Optional.ofNullable(violation);

    }

    public Optional<EntityConstraintViolation> validateDelete(int itemId) {

        EntityConstraintViolation violation = null;
        if (!itemRepository.existsById(itemId))
            violation = EntityConstraintViolation.fromException(new ItemNotFoundException(itemId));

        return Optional.ofNullable(violation);

    }

}
