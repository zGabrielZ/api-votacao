package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.exceptions.EntityInUseException;
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
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

    @Test
    @Order(6)
    void givenValidDataWhenUpdateThenReturnUpdatedAssociate() {
        AssociateEntity updateData = AssociateEntity.builder()
                .name("Updated Name")
                .email("updated@email.com")
                .password("UpdatedPassword@123")
                .documentNumber("33333333333")
                .build();

        AssociateDuplicateCheck emptyCheck = () -> 0L;
        when(repository.findByIdExternalUuid(associateId)).thenReturn(Optional.of(associateEntity));
        when(repository.existsEmail("updated@email.com")).thenReturn(emptyCheck);
        when(repository.save(any(AssociateEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssociateEntity updated = service.update(associateId, updateData);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@email.com", updated.getEmail());
        assertEquals("UpdatedPassword@123", updated.getPassword());
        verify(repository).findByIdExternalUuid(associateId);
        verify(repository).existsEmail("updated@email.com");
        verify(repository).save(any(AssociateEntity.class));
    }

    @Test
    @Order(7)
    void givenSameEmailWhenUpdateThenNotValidateDuplicate() {
        AssociateEntity updateData = AssociateEntity.builder()
                .name("Updated Name")
                .email("ana@email.com")
                .password("UpdatedPassword@123")
                .documentNumber("33333333333")
                .build();

        when(repository.findByIdExternalUuid(associateId)).thenReturn(Optional.of(associateEntity));
        when(repository.save(any(AssociateEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssociateEntity updated = service.update(associateId, updateData);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals("ana@email.com", updated.getEmail());
        verify(repository).findByIdExternalUuid(associateId);
        verify(repository).save(any(AssociateEntity.class));
    }

    @Test
    @Order(8)
    void givenExistingEmailWhenUpdateThenThrowBusinessException() {
        AssociateEntity updateData = AssociateEntity.builder()
                .name("Updated Name")
                .email("existing@email.com")
                .password("UpdatedPassword@123")
                .documentNumber("33333333333")
                .build();

        AssociateDuplicateCheck duplicateCheck = () -> 1L;
        when(repository.findByIdExternalUuid(associateId)).thenReturn(Optional.of(associateEntity));
        when(repository.existsEmail("existing@email.com")).thenReturn(duplicateCheck);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.update(associateId, updateData));

        assertEquals("Associate with this email already exists", exception.getMessage());
        verify(repository).findByIdExternalUuid(associateId);
        verify(repository).existsEmail("existing@email.com");
    }

    @Test
    @Order(9)
    void givenAssociateExistsWhenDeleteThenSuccess() {
        when(repository.findByIdExternalUuid(associateId)).thenReturn(Optional.of(associateEntity));

        service.delete(associateId);

        verify(repository).findByIdExternalUuid(associateId);
        verify(repository).delete(associateEntity);
        verify(repository).flush();
    }

    @Test
    @Order(10)
    void givenAssociateNotFoundWhenDeleteThenThrow() {
        when(repository.findByIdExternalUuid(associateId)).thenReturn(Optional.empty());

        AssociateNotFoundException ex = assertThrows(AssociateNotFoundException.class, () -> service.delete(associateId));

        assertEquals("Associate not found with ID: " + associateId, ex.getMessage());
        verify(repository).findByIdExternalUuid(associateId);
    }

    @Test
    @Order(11)
    void givenAssociateInUseWhenDeleteThenThrowEntityInUseException() {
        when(repository.findByIdExternalUuid(associateId)).thenReturn(Optional.of(associateEntity));
        doThrow(new DataIntegrityViolationException("Foreign key constraint"))
                .when(repository).delete(associateEntity);

        EntityInUseException ex = assertThrows(EntityInUseException.class, () -> service.delete(associateId));

        assertEquals("Cannot delete associate because it is in use", ex.getMessage());
        verify(repository).findByIdExternalUuid(associateId);
        verify(repository).delete(associateEntity);
    }
}
