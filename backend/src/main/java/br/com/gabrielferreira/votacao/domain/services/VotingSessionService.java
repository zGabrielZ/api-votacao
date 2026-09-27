package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AgendaNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.repositories.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VotingSessionService {

    private final VotingSessionRepository repository;

    private final AgendaService agendaService;

    @Transactional
    public VotingSessionEntity create(VotingSessionEntity entity) {
        validateVotingSessionDates(entity.getVotingStartTime(),  entity.getVotingEndTime());
        AgendaEntity agendaEntity = findAgendaById(entity.getAgenda().getIdExternalUuid());
        entity.setAgenda(agendaEntity);
        return repository.save(entity);
    }

    private void validateVotingSessionDates(OffsetDateTime start, OffsetDateTime end) {
        if (!end.isAfter(start)) {
            throw new BusinessException("Voting session end time must be after start time");
        }
    }

    private AgendaEntity findAgendaById(UUID id) {
        try {
            return agendaService.findById(id);
        } catch (AgendaNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
