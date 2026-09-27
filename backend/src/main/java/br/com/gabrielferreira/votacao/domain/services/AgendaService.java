package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AgendaNotFoundException;
import br.com.gabrielferreira.votacao.domain.repositories.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendaService {

    private final AgendaRepository repository;

    @Transactional
    public AgendaEntity create(AgendaEntity agendaEntity) {
        log.info("Request to save agenda : {}", agendaEntity);
        agendaEntity = repository.save(agendaEntity);
        log.info("Saved agenda : {}", agendaEntity);
        return agendaEntity;
    }

    public AgendaEntity findById(UUID id) {
        log.info("Request to find agenda by id : {}", id);
        AgendaEntity agendaEntity = repository.findByIdExternalUuid(id)
                .orElseThrow(() -> new AgendaNotFoundException(id));
        log.info("Found agenda : {}", agendaEntity);
        return agendaEntity;
    }
}