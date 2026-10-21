package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.repositories.AssociateRepository;
import br.com.gabrielferreira.votacao.domain.repositories.projection.AssociateDuplicateCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for AssociateService")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssociateServiceTest {

    @InjectMocks
    private AssociateService service;

    @Mock
    private AssociateRepository repository;

    private AssociateEntity associateEntity;

    private UUID associateId;

    @BeforeEach
    void setUp() {
        associateId = UUID.fromString("33333333-3333-4333-8333-333333333333");
        associateEntity = AssociateEntity.builder()
                .id(1L)
                .idExternalUuid(associateId)
                .name("Ana Costa")
                .email("ana@email.com")
                .password("123456")
                .documentNumber("33333333333")
                .build();
    }

    @Test
    @Order(1)
    void givenAssociateExistsWhenFindByIdThenReturnAssociate() {
        when(repository.findByIdExternalUuid(associateId))
                .thenReturn(Optional.of(associateEntity));

        AssociateEntity found = service.findById(associateId);

        assertNotNull(found);
        assertEquals(associateId, found.getIdExternalUuid());
        assertEquals("Ana Costa", found.getName());
        verify(repository).findByIdExternalUuid(associateId);
    }

    @Test
    @Order(2)
    void givenAssociateNotFoundWhenFindByIdThenThrow() {
        when(repository.findByIdExternalUuid(associateId))
                .thenReturn(Optional.empty());

        AssociateNotFoundException ex = assertThrows(AssociateNotFoundException.class, () -> service.findById(associateId));

        assertEquals("Associate not found with ID: " + associateId, ex.getMessage());
        verify(repository).findByIdExternalUuid(associateId);
    }

    @Test
    @Order(3)
    void givenNoDuplicateDataWhenCreateThenReturnSavedAssociate() {
        AssociateEntity newAssociate = AssociateEntity.builder()
                .name("Maria Souza")
                .email("maria@email.com")
                .password("Password@123")
                .documentNumber("12345678909")
                .build();

        AssociateDuplicateCheck emptyCheck = () -> 0L;
        when(repository.existsDocumentNumber("12345678909")).thenReturn(emptyCheck);
        when(repository.existsEmail("maria@email.com")).thenReturn(emptyCheck);
        when(repository.save(any(AssociateEntity.class))).thenAnswer(invocation -> {
            AssociateEntity entity = invocation.getArgument(0);
            entity.setId(10L);
            entity.setIdExternalUuid(UUID.randomUUID());
            return entity;
        });

        AssociateEntity saved = service.create(newAssociate);

        assertNotNull(saved);
        assertEquals("Maria Souza", saved.getName());
        assertEquals("maria@email.com", saved.getEmail());
        assertEquals("12345678909", saved.getDocumentNumber());
        verify(repository).existsDocumentNumber("12345678909");
        verify(repository).existsEmail("maria@email.com");
        verify(repository).save(any(AssociateEntity.class));
    }

    @Test
    @Order(4)
    void givenExistingDocumentNumberWhenCreateThenThrowBusinessException() {
        AssociateEntity newAssociate = AssociateEntity.builder()
                .name("Maria Souza")
                .email("maria@email.com")
                .password("Password@123")
                .documentNumber("12345678909")
                .build();

        AssociateDuplicateCheck duplicateCheck = () -> 1L;
        when(repository.existsDocumentNumber("12345678909")).thenReturn(duplicateCheck);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.create(newAssociate));

        assertEquals("Associate with this document number already exists", exception.getMessage());
        verify(repository).existsDocumentNumber("12345678909");
    }

    @Test
    @Order(5)
    void givenExistingEmailWhenCreateThenThrowBusinessException() {
        AssociateEntity newAssociate = AssociateEntity.builder()
                .name("Maria Souza")
                .email("maria@email.com")
                .password("Password@123")
                .documentNumber("12345678909")
                .build();

        AssociateDuplicateCheck emptyCheck = () -> 0L;
        AssociateDuplicateCheck duplicateCheck = () -> 1L;
        when(repository.existsDocumentNumber("12345678909")).thenReturn(emptyCheck);
        when(repository.existsEmail("maria@email.com")).thenReturn(duplicateCheck);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.create(newAssociate));

        assertEquals("Associate with this email already exists", exception.getMessage());
        verify(repository).existsDocumentNumber("12345678909");
        verify(repository).existsEmail("maria@email.com");
    }
}
