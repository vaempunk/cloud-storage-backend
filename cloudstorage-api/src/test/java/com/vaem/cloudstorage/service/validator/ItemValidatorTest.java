package com.vaem.cloudstorage.service.validator;

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
import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.exception.notfound.ItemNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ItemUniquenessException;
import com.vaem.cloudstorage.repository.ContainerRepository;
import com.vaem.cloudstorage.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
public class ItemValidatorTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ContainerRepository containerRepository;

    @InjectMocks
    private ItemValidator itemValidator;

    private Item testItem;
    private Item oldItem;

    @BeforeEach
    public void initItems() {

        testItem = Item.builder()
                .id(1)
                .name("Item1")
                .size(123)
                .dateCreated(OffsetDateTime.now())
                .dateChanged(OffsetDateTime.now())
                .parentId(1)
                .build();
        oldItem = Item.builder()
                .id(1)
                .name("OldItem1")
                .size(123)
                .dateCreated(OffsetDateTime.now())
                .dateChanged(OffsetDateTime.now())
                .parentId(2)
                .build();

    }

    @Test
    public void givenItemIsUnique_WhenValidateAdd_DoesNotThrow() {

        given(itemRepository.existsByNameAndParentId(testItem.getName(), testItem.getParentId()))
                .willReturn(false);
        given(containerRepository.existsById(testItem.getParentId()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = itemValidator.validateAdd(testItem);

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenItemIsNotUnique_WhenValidateAdd_ThrowsItemAlreadyExists() {

        given(itemRepository.existsByNameAndParentId(testItem.getName(), testItem.getParentId()))
                .willReturn(true);
        lenient().when(containerRepository.existsById(testItem.getParentId()))
                .thenReturn(true);

        Optional<EntityConstraintViolation> violation = itemValidator.validateAdd(testItem);

        assertTrue(violation.isPresent());
        assertEquals(ItemUniquenessException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenItemParentDoesNotExist_WhenValidateAdd_ThrowsContainerNotFound() {

        given(itemRepository.existsByNameAndParentId(testItem.getName(), testItem.getParentId()))
                .willReturn(false);
        given(containerRepository.existsById(testItem.getParentId()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = itemValidator.validateAdd(testItem);

        assertTrue(violation.isPresent());
        assertEquals(ContainerNotFoundException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenItemIsUnique_WhenValidateUpdate_DoesNotThrow() {

        given(itemRepository.existsByNameAndParentId(testItem.getName(), testItem.getParentId()))
                .willReturn(false);
        given(containerRepository.existsById(testItem.getParentId()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = itemValidator.validateUpdate(testItem, oldItem);

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenItemIsNotUnique_WhenValidateUpdate_ThrowsItemAlreadyExists() {

        given(itemRepository.existsByNameAndParentId(testItem.getName(), testItem.getParentId()))
                .willReturn(true);
        lenient().when(containerRepository.existsById(testItem.getParentId()))
                .thenReturn(true);

        Optional<EntityConstraintViolation> violation = itemValidator.validateUpdate(testItem, oldItem);

        assertTrue(violation.isPresent());
        assertEquals(ItemUniquenessException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenItemParentDoesNotExist_WhenValidateUpdate_ThrowsContainerNotFound() {

        given(itemRepository.existsByNameAndParentId(testItem.getName(), testItem.getParentId()))
                .willReturn(false);
        given(containerRepository.existsById(testItem.getParentId()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = itemValidator.validateUpdate(testItem, oldItem);

        assertTrue(violation.isPresent());
        assertEquals(ContainerNotFoundException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenItemExists_WhenValidateDelete_DoesNotThrow() {

        given(itemRepository.existsById(testItem.getId()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = itemValidator.validateDelete(testItem.getId());

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenItemDoesNotExist_WhenValidateDelete_ThrowsItemNotFound() {

        given(itemRepository.existsById(testItem.getId()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = itemValidator.validateDelete(testItem.getId());

        assertTrue(violation.isPresent());
        assertEquals(ItemNotFoundException.class, violation.get().getViolationException().getClass());

    }

}
