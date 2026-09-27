package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.enums.VotingSessionStatus;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.exceptions.VotingSessionNotFoundException;
import br.com.gabrielferreira.votacao.domain.repositories.VotingSessionRepository;
import br.com.gabrielferreira.votacao.domain.repositories.projection.VotingSessionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotingSessionService {

    private final VotingSessionRepository repository;

    private final AgendaService agendaService;

    @Transactional
    public VotingSessionEntity create(VotingSessionEntity entity) {
        log.info("Creating voting session: {}", entity);
        validateVotingSessionDates(entity.getVotingStartTime(),  entity.getVotingEndTime());
        AgendaEntity agendaEntity = agendaService.findById(entity.getAgenda().getIdExternalUuid());
        entity.setAgenda(agendaEntity);
        entity = repository.save(entity);
        log.info("Created voting session: {}", entity);
        return entity;
    }

    public VotingSessionResult getVotingSessionResults(UUID id) {
        VotingSessionEntity votingSessionEntity = findById(id);

        if (!votingSessionEntity.getStatus().equals(VotingSessionStatus.CLOSED)) {
            throw new BusinessException("Voting session is still open");
        }

        return repository.findVotingSessionResults(id);
    }

    public VotingSessionEntity findById(UUID id) {
        log.info("Finding voting session by id: {}", id);
        VotingSessionEntity votingSessionEntity = repository.findByIdExternalUuid(id)
                .orElseThrow(() -> new VotingSessionNotFoundException(id));
        log.info("Found voting session: {}", votingSessionEntity);
        return votingSessionEntity;
    }

    private void validateVotingSessionDates(OffsetDateTime start, OffsetDateTime end) {
        if (!end.isAfter(start)) {
            throw new BusinessException("Voting session end time must be after start time");
        }
    }
}
