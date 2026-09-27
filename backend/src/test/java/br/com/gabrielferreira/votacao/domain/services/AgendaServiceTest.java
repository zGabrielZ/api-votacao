package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.repositories.AgendaRepository;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for AgendaService")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AgendaServiceTest {

    @InjectMocks
    private AgendaService service;

    @Mock
    private AgendaRepository repository;

    private AgendaEntity agendaEntity;

    @BeforeEach
    void setUp() {
        agendaEntity = AgendaEntity.builder()
                .title("Title")
                .description("Description")
                .build();
    }

    @Test
    @Order(1)
    void givenAgendaEntityWhenCreateThenSaveAndReturn() {
        AgendaEntity agendaEntitySaved = AgendaEntity.builder()
                .id(1L)
                .idExternalUuid(UUID.randomUUID())
                .title(agendaEntity.getTitle())
                .description(agendaEntity.getDescription())
                .build();
        when(repository.save(agendaEntity))
                .thenReturn(agendaEntitySaved);

        AgendaEntity created = service.create(agendaEntity);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertNotNull(created.getIdExternalUuid());
        verify(repository).save(agendaEntity);
    }
}
