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

import com.vaem.cloudstorage.entity.Container;
import com.vaem.cloudstorage.exception.notfound.ContainerNotFoundException;
import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ContainerUniquenessException;
import com.vaem.cloudstorage.repository.ContainerRepository;
import com.vaem.cloudstorage.repository.ModuleRepository;

@ExtendWith(MockitoExtension.class)
public class ContainerValidatorTest {

    @Mock
    private ContainerRepository containerRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ContainerValidator containerValidator;

    private Container testContainer;
    private Container oldSpyContainer;

    @BeforeEach
    public void initContainers() {

        testContainer = spy(Container.builder()
                .id(1)
                .name("Container1")
                .dateCreated(OffsetDateTime.now())
                .moduleId(1)
                .parentId(2)
                .build());
        oldSpyContainer = spy(Container.builder()
                .id(1)
                .name("OldContainer1")
                .dateCreated(OffsetDateTime.now())
                .moduleId(2)
                .parentId(3)
                .build());

    }

    @Test
    public void givenContainerIsValid_WhenValidateAdd_DoesNotThrow() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(false);
        given(containerRepository.existsById(testContainer.getParentId()))
                .willReturn(true);
        given(moduleRepository.existsById(testContainer.getModuleId()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = containerValidator.validateAdd(testContainer);

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenContainerIsNotUnique_WhenValidateAdd_ThrowsContainerAlreadyExists() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(true);
        lenient().when(containerRepository.existsById(testContainer.getParentId()))
                .thenReturn(true);
        lenient().when(moduleRepository.existsById(testContainer.getModuleId()))
                .thenReturn(true);

        Optional<EntityConstraintViolation> violation = containerValidator.validateAdd(testContainer);

        assertTrue(violation.isPresent());
        assertEquals(ContainerUniquenessException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenContainerParentDoesNotExist_WhenValidateAdd_ThrowsContainerNotFound() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(false);
        given(containerRepository.existsById(testContainer.getParentId()))
                .willReturn(false);
        lenient().when(moduleRepository.existsById(testContainer.getModuleId()))
                .thenReturn(true);

        Optional<EntityConstraintViolation> violation = containerValidator.validateAdd(testContainer);

        assertTrue(violation.isPresent());
        assertEquals(ContainerNotFoundException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenContainerModuleDoesNotExist_WhenValidateAdd_ThrowsModuleNotFound() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(false);
        given(containerRepository.existsById(testContainer.getParentId()))
                .willReturn(true);
        given(moduleRepository.existsById(testContainer.getModuleId()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = containerValidator.validateAdd(testContainer);

        assertTrue(violation.isPresent());
        assertEquals(ModuleNotFoundException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenContainerIsUnique_WhenValidateUpdate_DoesNotThrow() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(false);
        given(moduleRepository.existsById(testContainer.getModuleId()))
                .willReturn(true);
        given(containerRepository.existsById(testContainer.getParentId()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = containerValidator.validateUpdate(testContainer, oldSpyContainer);

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenContainerIsNotUnique_WhenValidateUpdate_ThrowsContainerAlreadyExists() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(true);
        lenient().when(moduleRepository.existsById(testContainer.getModuleId()))
                .thenReturn(true);
        lenient().when(containerRepository.existsById(testContainer.getParentId()))
                .thenReturn(true);

        Optional<EntityConstraintViolation> violation = containerValidator.validateUpdate(testContainer, oldSpyContainer);

        assertTrue(violation.isPresent());
        assertEquals(ContainerUniquenessException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenContainerParentDoesNotExist_WhenValidateUpdate_ThrowsContainerNotFound() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(false);
        lenient().when(moduleRepository.existsById(testContainer.getModuleId()))
                .thenReturn(true);
        given(containerRepository.existsById(testContainer.getParentId()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = containerValidator.validateUpdate(testContainer, oldSpyContainer);

        assertTrue(violation.isPresent());
        assertEquals(ContainerNotFoundException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenContainerModuleDoesNotExist_WhenValidateUpdate_ThrowsModuleNotFound() {

        given(containerRepository.existsByNameAndModuleIdAndParentId(
                testContainer.getName(),
                testContainer.getModuleId(),
                testContainer.getParentId()))
                .willReturn(false);
        given(moduleRepository.existsById(testContainer.getModuleId()))
                .willReturn(false);
        lenient().when(containerRepository.existsById(testContainer.getParentId()))
                .thenReturn(true);

        Optional<EntityConstraintViolation> violation = containerValidator.validateUpdate(testContainer, oldSpyContainer);

        assertTrue(violation.isPresent());
        assertEquals(ModuleNotFoundException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenContainerExists_WhenValidateDelete_DoesNotThrow() {

        given(containerRepository.existsById(testContainer.getId()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = containerValidator.validateDelete(testContainer.getId());

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenContainerDoesNotExist_WhenValidateDelete_ThrowsContainerNotFound() {

        given(containerRepository.existsById(testContainer.getId()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = containerValidator.validateDelete(testContainer.getId());

        assertTrue(violation.isPresent());
        assertEquals(ContainerNotFoundException.class, violation.get().getViolationException().getClass());

    }

}
