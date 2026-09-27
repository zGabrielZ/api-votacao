package br.com.gabrielferreira.votacao.domain.repositories;

import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgendaRepository extends JpaRepository<AgendaEntity, Long> {

    Optional<AgendaEntity> findByIdExternalUuid(UUID id);
}