package br.com.gabrielferreira.votacao.domain.repositories;

import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSessionEntity, Long> {

    Optional<VotingSessionEntity> findByIdExternalUuid(UUID votingSessionId);
}
