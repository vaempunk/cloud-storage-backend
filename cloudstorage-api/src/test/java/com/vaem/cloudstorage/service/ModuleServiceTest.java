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

import com.vaem.cloudstorage.entity.Module;
import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ModuleUniquenessException;
import com.vaem.cloudstorage.repository.ModuleRepository;
import com.vaem.cloudstorage.service.validator.EntityConstraintViolation;
import com.vaem.cloudstorage.service.validator.ModuleValidator;

@ExtendWith(MockitoExtension.class)
public class ModuleServiceTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private ModuleValidator moduleValidator;

    @InjectMocks
    private ModuleService moduleService;

    private Module spyModule;
    private Module oldSpyModule;

    @BeforeEach
    public void initModule() {

        spyModule = spy(Module.builder()
                .id(1)
                .name("Module1")
                .description("Description...")
                .dateCreated(OffsetDateTime.now())
                .build());

        oldSpyModule = spy(Module.builder()
                .id(1)
                .name("Module2")
                .description("Description...123")
                .dateCreated(OffsetDateTime.now())
                .build());

    }

    @Test
    public void givenModuleExist_WhenGetById_ReturnIt() {

        given(moduleRepository.findById(spyModule.getId()))
                .willReturn(Optional.of(spyModule));

        assertDoesNotThrow(() -> {
            Module foundModule = moduleService.getById(spyModule.getId());
            assertEquals(spyModule, foundModule);
        });

    }

    @Test
    public void givenModuleDoesNotExist_WhenGetById_ThrowsModuleNotFound() {

        given(moduleRepository.findById(spyModule.getId()))
                .willReturn(Optional.empty());

        assertThrows(ModuleNotFoundException.class, () -> {
            moduleService.getById(spyModule.getId());
        });

    }

    @Test
    public void givenModuleIsValid_WhenAdd_InvokesRepository() {

        given(moduleValidator.validateAdd(spyModule))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            moduleService.add(spyModule);
        });

        then(spyModule)
                .should()
                .setDateCreated(any(OffsetDateTime.class));
        then(moduleRepository)
                .should()
                .save(spyModule);

    }

    @Test
    public void givenModuleIsInvalid_WhenAdd_DoesNotInvokeRepository() {

        EntityConstraintViolation violation = EntityConstraintViolation.fromException(new ModuleUniquenessException());
        given(moduleValidator.validateAdd(spyModule))
                .willReturn(Optional.of(violation));

        assertThrows(ModuleUniquenessException.class, () -> {
            moduleService.add(spyModule);
        });

        then(spyModule)
                .should(never())
                .setDateCreated(any(OffsetDateTime.class));
        then(moduleRepository)
                .should(never())
                .save(any(Module.class));

    }

    @Test
    public void givenModuleIsValidate_WhenUpdate_InvokesRepository() {

        given(moduleRepository.findById(spyModule.getId()))
                .willReturn(Optional.of(oldSpyModule));
        given(moduleValidator.validateUpdate(spyModule, oldSpyModule))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            moduleService.update(spyModule);
        });

        then(moduleRepository)
                .should()
                .save(oldSpyModule);

    }

    @Test
    public void givenModuleIsInvalid_WhenUpdate_DoesNotInvokeRepository() {

        given(moduleRepository.findById(spyModule.getId()))
                .willReturn(Optional.of(oldSpyModule));
        EntityConstraintViolation violation = EntityConstraintViolation.fromException(new ModuleUniquenessException());
        given(moduleValidator.validateUpdate(spyModule, oldSpyModule))
                .willReturn(Optional.of(violation));

        assertThrows(ModuleUniquenessException.class, () -> {
            moduleService.update(spyModule);
        });

        then(moduleRepository)
                .should(never())
                .save(any(Module.class));

    }

    @Test
    public void givenModuleExist_WhenDelete_InvokesRepository() {

        given(moduleValidator.validateDelete(spyModule.getId()))
                .willReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            moduleService.deleteById(spyModule.getId());
        });

        then(moduleRepository)
                .should()
                .deleteById(spyModule.getId());

    }

    @Test
    public void givenModuleDoesNotExist_WhenDelete_DoesNotInvokeRepository() {

        EntityConstraintViolation violation = EntityConstraintViolation.fromException(
                new ModuleNotFoundException(spyModule.getId()));
        given(moduleValidator.validateDelete(spyModule.getId()))
                .willReturn(Optional.of(violation));

        assertThrows(ModuleNotFoundException.class, () -> {
            moduleService.deleteById(spyModule.getId());
        });

        then(moduleRepository)
                .should(never())
                .deleteById(anyInt());

    }

}
