package com.vaem.cloudstorage.service;

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
import com.vaem.cloudstorage.exception.uniqueness.ContainerUniquenessException;
import com.vaem.cloudstorage.repository.ContainerRepository;
import com.vaem.cloudstorage.service.validator.ContainerValidator;
import com.vaem.cloudstorage.service.validator.EntityConstraintViolation;

@ExtendWith(MockitoExtension.class)
public class ContainerServiceTest {

    @Mock
    private ContainerRepository containerRepository;

    @Mock
    private ContainerValidator containerValidator;

    @InjectMocks
    private ContainerService containerService;

    private Container spyContainer;
    private Container oldSpyContainer;

    @BeforeEach
    public void initContainer() {

        spyContainer = spy(Container.builder()
                .id(1)
                .name("testContainer")
                .dateCreated(OffsetDateTime.now())
                .moduleId(1)
                .parentId(1)
                .build());

        oldSpyContainer = spy(Container.builder()
                .id(1)
                .name("testContainer2")
                .dateCreated(OffsetDateTime.now())
                .moduleId(2)
                .parentId(2)
                .build());

    }

    @Test
    public void givenContainerExists_WhenGetById_ReturnIt() {

        given(containerRepository.findById(spyContainer.getId()))
                .willReturn(Optional.of(spyContainer));

        assertDoesNotThrow(() -> {
            Container container = containerService.getById(spyContainer.getId());
            assertEquals(spyContainer, container);
        });
    }

    @Test
    public void givenContainerDoesNotExist_WhenGetById_ThrowsContainerNotFound() {

        given(containerRepository.findById(spyContainer.getId()))
                .willReturn(Optional.empty());

        assertThrows(ContainerNotFoundException.class, () -> {
            containerService.getById(spyContainer.getId());
        });

    }

    @Test
    public void givenContainerIsValid_WhenAdd_InvokesRepository() {

        given(containerValidator.validateAdd(spyContainer))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            containerService.add(spyContainer);
        });

        then(spyContainer)
                .should()
                .setDateCreated(any(OffsetDateTime.class));
        then(containerRepository)
                .should()
                .save(spyContainer);

    }

    @Test
    public void givenContainerIsInvalid_WhenAdd_DoesNotInvokeRepository() {

        EntityConstraintViolation violation = EntityConstraintViolation.fromException(new ContainerUniquenessException());
        given(containerValidator.validateAdd(spyContainer))
                .willReturn(Optional.of(violation));

        assertThrows(ContainerUniquenessException.class, () -> {
            containerService.add(spyContainer);
        });

        then(containerRepository)
                .should(never())
                .save(any(Container.class));

    }

    @Test
    public void givenContainerExists_WhenUpdate_InvokesRepository() {

        given(containerRepository.findById(spyContainer.getId()))
                .willReturn(Optional.of(oldSpyContainer));
        given(containerValidator.validateUpdate(spyContainer, oldSpyContainer))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            containerService.update(spyContainer);
        });

        then(containerRepository)
                .should()
                .save(oldSpyContainer);

    }

    @Test
    public void givenContainerIsNotUnique_WhenUpdate_DoesNotInvokeRepository() {

        given(containerRepository.findById(spyContainer.getId()))
                .willReturn(Optional.of(oldSpyContainer));
        EntityConstraintViolation violation = EntityConstraintViolation.fromException(new ContainerUniquenessException());
        given(containerValidator.validateUpdate(spyContainer, oldSpyContainer))
                .willReturn(Optional.of(violation));

        assertThrows(ContainerUniquenessException.class, () -> {
            containerService.update(spyContainer);
        });

        then(containerRepository)
                .should(never())
                .save(any(Container.class));

    }

    @Test
    public void givenContainerExists_WhenDelete_InvokesRepository() {

        given(containerValidator.validateDelete(spyContainer.getId()))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            containerService.deleteById(spyContainer.getId());
        });

        then(containerRepository)
                .should()
                .deleteById(spyContainer.getId());

    }

    @Test
    public void givenContainerDoesNotExist_WhenDelete_DoesNotInvokeRepository() {

        EntityConstraintViolation violation = EntityConstraintViolation.fromException(
                new ContainerNotFoundException(spyContainer.getId()));
        given(containerValidator.validateDelete(spyContainer.getId()))
                .willReturn(Optional.of(violation));

        assertThrows(ContainerNotFoundException.class, () -> {
            containerService.deleteById(spyContainer.getId());
        });

        then(containerRepository)
                .should(never())
                .deleteById(anyInt());

    }

}
