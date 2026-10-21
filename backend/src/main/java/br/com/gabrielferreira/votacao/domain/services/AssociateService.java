package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.repositories.AssociateRepository;
import br.com.gabrielferreira.votacao.domain.repositories.projection.AssociateDuplicateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssociateService {

    private final AssociateRepository repository;

    @Transactional
    public AssociateEntity create(AssociateEntity associateEntity) {
        log.info("Request to save associate : {}", associateEntity);

        validateDuplicateDocumentNumber(associateEntity.getDocumentNumber());
        validateDuplicateEmail(associateEntity.getEmail());

        associateEntity = repository.save(associateEntity);
        log.info("Saved associate : {}", associateEntity);
        return associateEntity;
    }

    public AssociateEntity findById(UUID id) {
        log.info("Request to find associate by id : {}", id);
        AssociateEntity associateEntity = repository.findByIdExternalUuid(id)
                .orElseThrow(() -> new AssociateNotFoundException(id));
        log.info("Found associate : {}", associateEntity);
        return associateEntity;
    }

    private void validateDuplicateDocumentNumber(String documentNumber) {
        AssociateDuplicateCheck result = repository.existsDocumentNumber(documentNumber);
        if (result != null && result.getTotalMatches() > 0) {
            throw new BusinessException("Associate with this document number already exists");
        }
    }

    private void validateDuplicateEmail(String email) {
        AssociateDuplicateCheck result = repository.existsEmail(email);
        if (result != null && result.getTotalMatches() > 0) {
            throw new BusinessException("Associate with this email already exists");
        }
    }
}
