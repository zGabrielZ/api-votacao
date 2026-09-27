package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.repositories.AssociateRepository;
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
}
