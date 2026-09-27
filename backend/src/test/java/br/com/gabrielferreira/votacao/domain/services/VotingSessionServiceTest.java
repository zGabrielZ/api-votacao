package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.enums.VotingSessionStatus;
import br.com.gabrielferreira.votacao.domain.exceptions.AgendaNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.exceptions.VotingSessionNotFoundException;
import br.com.gabrielferreira.votacao.domain.repositories.VotingSessionRepository;
import br.com.gabrielferreira.votacao.domain.repositories.projection.VotingSessionResult;
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

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for VotingSessionService")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VotingSessionServiceTest {

    @InjectMocks
    private VotingSessionService service;

    @Mock
    private VotingSessionRepository repository;

    @Mock
    private AgendaService agendaService;

    private VotingSessionEntity votingSessionEntity;

    private AgendaEntity agendaEntity;

    @BeforeEach
    void setUp() {
        agendaEntity = AgendaEntity.builder()
                .id(1L)
                .idExternalUuid(UUID.fromString("223e4567-e89b-12d3-a456-426614174111"))
                .title("Title")
                .description("Description")
                .build();

        votingSessionEntity = VotingSessionEntity.builder()
                .votingStartTime(OffsetDateTime.parse("2026-01-01T10:00:00Z"))
                .votingEndTime(OffsetDateTime.parse("2026-01-01T11:00:00Z"))
                .agenda(AgendaEntity.builder().idExternalUuid(agendaEntity.getIdExternalUuid()).build())
                .build();
    }

    @Test
    @Order(1)
    void givenValidVotingSessionWhenCreateThenSaveAndReturn() {
        when(agendaService.findById(agendaEntity.getIdExternalUuid()))
                .thenReturn(agendaEntity);
        VotingSessionEntity votingSessionEntitySaved = VotingSessionEntity.builder()
                .id(1L)
                .idExternalUuid(UUID.randomUUID())
                .votingStartTime(votingSessionEntity.getVotingStartTime())
                .votingEndTime(votingSessionEntity.getVotingEndTime())
                .agenda(agendaEntity)
                .status(VotingSessionStatus.OPEN)
                .build();

        when(repository.save(votingSessionEntity))
                .thenReturn(votingSessionEntitySaved);

        VotingSessionEntity created = service.create(votingSessionEntity);

        assertNotNull(created);
        assertEquals(votingSessionEntity.getVotingStartTime(), created.getVotingStartTime());
        assertEquals(votingSessionEntity.getVotingEndTime(), created.getVotingEndTime());
        assertEquals(votingSessionEntity.getAgenda().getId(), created.getAgenda().getId());
        assertEquals(VotingSessionStatus.OPEN, created.getStatus());
        verify(repository).save(any());
    }

    @Test
    @Order(2)
    void givenInvalidDatesWhenCreateThenThrow() {
        votingSessionEntity.setVotingEndTime(votingSessionEntity.getVotingStartTime().minusHours(1));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(votingSessionEntity));
        assertEquals("Voting session end time must be after start time", ex.getMessage());
    }

    @Test
    @Order(3)
    void givenVotingSessionExistsWhenFindByIdThenReturn() {
        UUID votingSessionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174111");
        VotingSessionEntity saved = VotingSessionEntity.builder()
                .id(2L)
                .idExternalUuid(votingSessionId)
                .votingStartTime(votingSessionEntity.getVotingStartTime())
                .votingEndTime(votingSessionEntity.getVotingEndTime())
                .agenda(agendaEntity)
                .status(VotingSessionStatus.OPEN)
                .build();

        when(repository.findByIdExternalUuid(votingSessionId))
                .thenReturn(Optional.of(saved));

        VotingSessionEntity found = service.findById(votingSessionId);

        assertNotNull(found);
        assertEquals(votingSessionId, found.getIdExternalUuid());
        assertEquals(VotingSessionStatus.OPEN, found.getStatus());
        verify(repository).findByIdExternalUuid(votingSessionId);
    }

    @Test
    @Order(4)
    void givenVotingSessionNotFoundWhenFindByIdThenThrow() {
        UUID votingSessionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174222");
        when(repository.findByIdExternalUuid(votingSessionId))
                .thenReturn(Optional.empty());

        VotingSessionNotFoundException ex = assertThrows(VotingSessionNotFoundException.class, () -> service.findById(votingSessionId));

        assertTrue(ex.getMessage().contains("Voting session not found with ID"));
    }

    @Test
    @Order(5)
    void givenAgendaNotFoundWhenCreateThenThrow() {
        when(agendaService.findById(votingSessionEntity.getAgenda().getIdExternalUuid()))
                .thenThrow(new AgendaNotFoundException(agendaEntity.getIdExternalUuid()));

        AgendaNotFoundException ex = assertThrows(AgendaNotFoundException.class, () -> service.create(votingSessionEntity));
        assertTrue(ex.getMessage().contains("Agenda not found with ID"));
    }

    @Test
    @Order(6)
    void givenClosedVotingSessionWhenGetVotingSessionResultsThenReturnProjection() {
        UUID votingSessionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174333");
        VotingSessionEntity closedSession = VotingSessionEntity.builder()
                .idExternalUuid(votingSessionId)
                .status(VotingSessionStatus.CLOSED)
                .build();

        VotingSessionResult result = new VotingSessionResult() {
            @Override
            public Long getYesVotes() {
                return 10L;
            }

            @Override
            public Long getNoVotes() {
                return 4L;
            }

            @Override
            public Long getTotalVotes() {
                return 14L;
            }
        };

        when(repository.findByIdExternalUuid(votingSessionId)).thenReturn(Optional.of(closedSession));
        when(repository.findVotingSessionResults(votingSessionId)).thenReturn(result);

        VotingSessionResult response = service.getVotingSessionResults(votingSessionId);

        assertNotNull(response);
        assertEquals(10L, response.getYesVotes());
        assertEquals(4L, response.getNoVotes());
        assertEquals(14L, response.getTotalVotes());
    }

    @Test
    @Order(7)
    void givenOpenVotingSessionWhenGetVotingSessionResultsThenThrowBusinessException() {
        UUID votingSessionId = UUID.fromString("123e4567-e89b-12d3-a456-426614174444");
        VotingSessionEntity openSession = VotingSessionEntity.builder()
                .idExternalUuid(votingSessionId)
                .status(VotingSessionStatus.OPEN)
                .build();

        when(repository.findByIdExternalUuid(votingSessionId)).thenReturn(Optional.of(openSession));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.getVotingSessionResults(votingSessionId));

        assertEquals("Voting session is still open", exception.getMessage());
    }
}
