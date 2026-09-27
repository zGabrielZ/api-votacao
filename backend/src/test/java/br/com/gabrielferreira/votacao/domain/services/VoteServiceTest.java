package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.entities.VoteEntity;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.enums.VoteOption;
import br.com.gabrielferreira.votacao.domain.enums.VotingSessionStatus;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.repositories.VoteRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for VoteService")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VoteServiceTest {

    @InjectMocks
    private VoteService service;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VotingSessionService votingSessionService;

    @Mock
    private AssociateService associateService;

    private VoteEntity voteEntity;

    private VotingSessionEntity votingSessionEntity;

    private AssociateEntity associateEntity;

    @BeforeEach
    void setUp() {
        UUID votingSessionId = UUID.fromString("d80b7f1f-c43e-4fa0-a772-e3c14eef9730");
        UUID associateId = UUID.fromString("8f978122-abba-43b4-bb85-59cc3a7c0bd9");

        votingSessionEntity = VotingSessionEntity.builder()
                .idExternalUuid(votingSessionId)
                .votingStartTime(OffsetDateTime.now().minusMinutes(10))
                .votingEndTime(OffsetDateTime.now().plusMinutes(30))
                .status(VotingSessionStatus.OPEN)
                .build();

        associateEntity = AssociateEntity.builder()
                .idExternalUuid(associateId)
                .name("Maria Silva")
                .email("maria@email.com")
                .password("123456")
                .documentNumber("11111111111")
                .build();

        voteEntity = VoteEntity.builder()
                .votingSession(VotingSessionEntity.builder().idExternalUuid(votingSessionId).build())
                .associate(AssociateEntity.builder().idExternalUuid(associateId).build())
                .voteOption(VoteOption.YES)
                .build();
    }

    @Test
    @Order(1)
    void givenValidVoteWhenCreateThenSaveAndReturn() {
        when(votingSessionService.findById(voteEntity.getVotingSession().getIdExternalUuid()))
                .thenReturn(votingSessionEntity);
        when(associateService.findById(voteEntity.getAssociate().getIdExternalUuid()))
                .thenReturn(associateEntity);
        when(voteRepository.existsByVotingSessionAndAssociate(votingSessionEntity, associateEntity))
                .thenReturn(false);
        when(voteRepository.save(voteEntity)).thenReturn(voteEntity);

        VoteEntity created = service.create(voteEntity);

        assertNotNull(created);
        assertEquals(votingSessionEntity, created.getVotingSession());
        assertEquals(associateEntity, created.getAssociate());
        assertEquals(VoteOption.YES, created.getVoteOption());
        verify(voteRepository).save(voteEntity);
    }

    @Test
    @Order(2)
    void givenVotingSessionClosedWhenCreateThenThrow() {
        votingSessionEntity.setStatus(VotingSessionStatus.CLOSED);

        when(votingSessionService.findById(voteEntity.getVotingSession().getIdExternalUuid()))
                .thenReturn(votingSessionEntity);
        when(associateService.findById(voteEntity.getAssociate().getIdExternalUuid()))
                .thenReturn(associateEntity);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(voteEntity));

        assertEquals("Voting session is not open", ex.getMessage());
    }

    @Test
    @Order(3)
    void givenAssociateAlreadyVotedWhenCreateThenThrow() {
        when(votingSessionService.findById(voteEntity.getVotingSession().getIdExternalUuid()))
                .thenReturn(votingSessionEntity);
        when(associateService.findById(voteEntity.getAssociate().getIdExternalUuid()))
                .thenReturn(associateEntity);
        when(voteRepository.existsByVotingSessionAndAssociate(votingSessionEntity, associateEntity))
                .thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(voteEntity));

        assertEquals("Associate has already voted in this session", ex.getMessage());
    }

    @Test
    @Order(4)
    void givenAssociateNotFoundWhenCreateThenThrow() {
        when(votingSessionService.findById(voteEntity.getVotingSession().getIdExternalUuid()))
                .thenReturn(votingSessionEntity);
        when(associateService.findById(voteEntity.getAssociate().getIdExternalUuid()))
                .thenThrow(new AssociateNotFoundException(voteEntity.getAssociate().getIdExternalUuid()));

        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(voteEntity));

        assertTrue(ex.getMessage().contains("Associate not found with ID"));
    }
}
