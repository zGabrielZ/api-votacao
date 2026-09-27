package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.repositories.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}