package com.vaem.cloudstorage.service.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaem.cloudstorage.entity.Module;
import com.vaem.cloudstorage.exception.notfound.ModuleNotFoundException;
import com.vaem.cloudstorage.exception.uniqueness.ModuleUniquenessException;
import com.vaem.cloudstorage.repository.ModuleRepository;

@ExtendWith(MockitoExtension.class)
public class ModuleValidatorTest {

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleValidator moduleValidator;

    private static Module testModule;
    private static Module oldModule;

    @BeforeAll
    public static void initModules() {

        testModule = spy(Module.builder()
                .id(1)
                .name("Module1")
                .description("Description1")
                .build());

        oldModule = spy(Module.builder()
                .id(testModule.getId())
                .name("OldModule1")
                .description("Some desc...")
                .dateCreated(OffsetDateTime.now())
                .build());

    }

    @Test
    public void givenModuleIsUnique_WhenValidateAdd_DoesNotThrow() {

        given(moduleRepository.existsByName(testModule.getName()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = moduleValidator.validateAdd(testModule);

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenModuleIsNotUnique_WhenValidateAdd_ThrowsModuleAlreadyExists() {

        given(moduleRepository.existsByName(testModule.getName()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = moduleValidator.validateAdd(testModule);

        assertTrue(violation.isPresent());
        assertEquals(ModuleUniquenessException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenModuleIsUnique_WhenValidateUpdate_DoesNotThrow() {

        given(moduleRepository.existsByName(testModule.getName()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = moduleValidator.validateUpdate(testModule, oldModule);

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenModuleIsNotUnique_WhenValidateUpdate_ThrowsModuleAlreadyExists() {

        given(moduleRepository.existsByName(testModule.getName()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = moduleValidator.validateUpdate(testModule, oldModule);

        assertTrue(violation.isPresent());
        assertEquals(ModuleUniquenessException.class, violation.get().getViolationException().getClass());

    }

    @Test
    public void givenModuleExists_WhenValidateDelete_DoesNotThrow() {

        given(moduleRepository.existsById(testModule.getId()))
                .willReturn(true);

        Optional<EntityConstraintViolation> violation = moduleValidator.validateDelete(testModule.getId());

        assertTrue(violation.isEmpty());

    }

    @Test
    public void givenModuleDoesNotExist_WhenValidateDelete_ThrowsModuleNotFound() {

        given(moduleRepository.existsById(testModule.getId()))
                .willReturn(false);

        Optional<EntityConstraintViolation> violation = moduleValidator.validateDelete(testModule.getId());

        assertTrue(violation.isPresent());
        assertEquals(ModuleNotFoundException.class, violation.get().getViolationException().getClass());

    }

}
