package br.com.gabrielferreira.votacao.domain.repositories;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssociateRepository extends JpaRepository<AssociateEntity, Long> {

    Optional<AssociateEntity> findByIdExternalUuid(UUID externalUuid);
}
