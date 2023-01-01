package com.vaem.cloudstorage.service;

import static org.mockito.Mockito.spy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaem.cloudstorage.entity.Item;
import com.vaem.cloudstorage.exception.notfound.ItemNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ItemUniquenessException;
import com.vaem.cloudstorage.repository.ItemRepository;
import com.vaem.cloudstorage.service.validator.EntityConstraintViolation;
import com.vaem.cloudstorage.service.validator.ItemValidator;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemValidator itemValidator;

    @InjectMocks
    private ItemService itemService;

    private Item spyItem;
    private Item oldSpyItem;

    @BeforeEach
    public void initItem() {

        spyItem = spy(Item.builder()
                .id(1)
                .name("testItem")
                .size(123)
                .dateCreated(OffsetDateTime.now())
                .dateChanged(OffsetDateTime.now())
                .parentId(1)
                .build());

        oldSpyItem = spy(Item.builder()
                .id(1)
                .name("testItem2")
                .size(1234)
                .dateCreated(OffsetDateTime.now())
                .dateChanged(OffsetDateTime.now())
                .parentId(2)
                .build());

    }

    @Test
    public void givenItemExists_WhenGetById_ReturnIt() {

        given(itemRepository.findById(spyItem.getId()))
                .willReturn(Optional.of(spyItem));

        assertDoesNotThrow(() -> {
            Item foundItem = itemService.getById(spyItem.getId());
            assertEquals(spyItem, foundItem);
        });

    }

    @Test
    public void givenItemDoesNotExist_WhenGetById_ThrowsItemNotFound() {

        given(itemRepository.findById(spyItem.getId()))
                .willReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> {
            itemService.getById(spyItem.getId());
        });

    }

    @Test
    public void givenItemIsValid_WhenAdd_InvokesRepository() {

        given(itemValidator.validateAdd(spyItem))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            itemService.add(spyItem);
        });

        then(itemRepository)
                .should()
                .save(spyItem);

    }

    @Test
    public void givenItemIsInvalid_WhenAdd_DoesNotInvokeRepository() {

        EntityConstraintViolation violation = EntityConstraintViolation.fromException(new ItemUniquenessException());
        given(itemValidator.validateAdd(spyItem))
                .willReturn(Optional.of(violation));

        assertThrows(ItemUniquenessException.class, () -> {
            itemService.add(spyItem);
        });

        then(itemRepository)
                .should(never())
                .save(any(Item.class));

    }

    @Test
    public void givenItemIsValid_WhenUpdate_InvokesRepository() {

        given(itemRepository.findById(spyItem.getId()))
                .willReturn(Optional.of(oldSpyItem));
        given(itemValidator.validateUpdate(spyItem, oldSpyItem))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            itemService.update(spyItem);
        });

        then(itemRepository)
                .should()
                .save(oldSpyItem);

    }

    @Test
    public void givenItemIsInvalid_WhenUpdate_DoesNotInvokeRepository() {

        given(itemRepository.findById(spyItem.getId()))
                .willReturn(Optional.of(oldSpyItem));
        EntityConstraintViolation violation = EntityConstraintViolation.fromException(new ItemUniquenessException());
        given(itemValidator.validateUpdate(spyItem, oldSpyItem))
                .willReturn(Optional.of(violation));

        assertThrows(ItemUniquenessException.class, () -> {
            itemService.update(spyItem);
        });

        then(itemRepository)
                .should(never())
                .save(any(Item.class));

    }

    @Test
    public void givenItemExists_WhenDeleteById_InvokesRepository() {

        given(itemValidator.validateDelete(spyItem.getId()))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            itemService.deleteById(spyItem.getId());
        });

        then(itemRepository)
                .should()
                .deleteById(spyItem.getId());

    }

    @Test
    public void givenItemDoesNotExist_WhenDeleteById_DoesNotInvokeRepository() {

        EntityConstraintViolation violation = EntityConstraintViolation.fromException(
                new ItemNotFoundException(spyItem.getId()));
        given(itemValidator.validateDelete(spyItem.getId()))
                .willReturn(Optional.of(violation));

        assertThrows(ItemNotFoundException.class, () -> {
            itemService.deleteById(spyItem.getId());
        });

        then(itemRepository)
                .should(never())
                .deleteById(anyInt());

    }

}
