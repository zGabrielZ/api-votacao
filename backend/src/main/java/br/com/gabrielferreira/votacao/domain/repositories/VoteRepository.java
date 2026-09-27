package br.com.gabrielferreira.votacao.domain.repositories;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.entities.VoteEntity;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    boolean existsByVotingSessionAndAssociate(VotingSessionEntity votingSession, AssociateEntity associate);
}
