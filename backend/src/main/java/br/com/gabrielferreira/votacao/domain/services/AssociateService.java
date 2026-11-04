package br.com.gabrielferreira.votacao.domain.services;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.exceptions.AssociateNotFoundException;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.exceptions.EntityInUseException;
import br.com.gabrielferreira.votacao.domain.repositories.AssociateRepository;
import br.com.gabrielferreira.votacao.domain.repositories.filter.associate.AssociateFilter;
import br.com.gabrielferreira.votacao.domain.repositories.projection.AssociateDuplicateCheck;
import br.com.gabrielferreira.votacao.domain.specs.AssociateSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public AssociateEntity update(UUID id, AssociateEntity associateEntity) {
        log.info("Request to update associate with id : {}", id);
        AssociateEntity existingAssociate = findById(id);

        if (!existingAssociate.getEmail().equals(associateEntity.getEmail())) {
            validateDuplicateEmail(associateEntity.getEmail());
        }

        existingAssociate.setName(associateEntity.getName());
        existingAssociate.setEmail(associateEntity.getEmail());
        existingAssociate.setPassword(associateEntity.getPassword());
        existingAssociate = repository.save(existingAssociate);
        log.info("Updated associate : {}", existingAssociate);
        return existingAssociate;
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Request to delete associate with id : {}", id);
        AssociateEntity associateEntity = findById(id);

        try {
            repository.delete(associateEntity);
            repository.flush();
            log.info("Deleted associate with id : {}", id);
        } catch (DataIntegrityViolationException ex) {
            log.error("Associate with id : {} is in use", id);
            throw new EntityInUseException("Cannot delete associate because it is in use");
        }
    }

    public Page<AssociateEntity> findAll(AssociateFilter filter, Pageable pageable) {
        log.info("Request to find all associates with filter : {} and pageable : {}", filter, pageable);
        Page<AssociateEntity> associates = repository.findAll(new AssociateSpec(filter), pageable);
        log.info("Found {} associates", associates.getTotalElements());
        return associates;
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
