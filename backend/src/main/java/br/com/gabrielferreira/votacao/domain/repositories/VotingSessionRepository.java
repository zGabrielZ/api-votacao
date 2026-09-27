package br.com.gabrielferreira.votacao.domain.repositories;

import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSessionEntity, Long> {

}
