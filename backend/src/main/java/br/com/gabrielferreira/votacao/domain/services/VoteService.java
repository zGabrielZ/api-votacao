package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.entities.VoteEntity;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.enums.VotingSessionStatus;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;

    private final VotingSessionService votingSessionService;

    private final AssociateService associateService;

    @Transactional
    public VoteEntity create(VoteEntity voteEntity) {
        log.info("Creating vote for voting session: {}", voteEntity.getVotingSession().getIdExternalUuid());
        VotingSessionEntity votingSession = votingSessionService.findById(voteEntity.getVotingSession().getIdExternalUuid());
        voteEntity.setVotingSession(votingSession);

        AssociateEntity associate = findByAssociate(voteEntity.getAssociate().getIdExternalUuid());
        voteEntity.setAssociate(associate);

        validateVotingSessionIsOpen(votingSession);
        validateAssociateHasNotAlreadyVoted(votingSession, associate);

        voteEntity = voteRepository.save(voteEntity);
        log.info("Created vote for voting session: {}", voteEntity);
        return voteEntity;
    }

    private AssociateEntity findByAssociate(UUID associateId) {
        try {
            return associateService.findById(associateId);
        } catch (AssociateNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void validateVotingSessionIsOpen(VotingSessionEntity votingSession) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (votingSession.getStatus() != VotingSessionStatus.OPEN
                || now.isBefore(votingSession.getVotingStartTime())
                || now.isAfter(votingSession.getVotingEndTime())) {
            throw new BusinessException("Voting session is not open");
        }
    }

    private void validateAssociateHasNotAlreadyVoted(VotingSessionEntity votingSession, AssociateEntity associate) {
        if (voteRepository.existsByVotingSessionAndAssociate(votingSession, associate)) {
            throw new BusinessException("Associate has already voted in this session");
        }
    }
}
