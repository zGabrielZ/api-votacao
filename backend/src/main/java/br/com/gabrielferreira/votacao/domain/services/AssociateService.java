package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.repositories.AssociateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssociateService {

    private final AssociateRepository repository;

    public AssociateEntity findById(UUID id) {
        log.info("Request to find associate by id : {}", id);
        AssociateEntity associateEntity = repository.findByIdExternalUuid(id)
                .orElseThrow(() -> new AssociateNotFoundException(id));
        log.info("Found associate : {}", associateEntity);
        return associateEntity;
    }
}
