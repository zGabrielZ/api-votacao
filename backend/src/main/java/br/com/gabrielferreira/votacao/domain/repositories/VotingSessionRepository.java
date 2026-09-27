package br.com.gabrielferreira.votacao.domain.repositories;

import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.repositories.projection.VotingSessionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSessionEntity, Long> {

    Optional<VotingSessionEntity> findByIdExternalUuid(UUID votingSessionId);

    @Query(value = "select SUM(CASE WHEN VOTE_OPTION = 'YES' THEN 1 ELSE 0 END) AS yesVotes, " +
            "SUM(CASE WHEN VOTE_OPTION = 'NO' THEN 1 ELSE 0 END) AS noVotes, " +
            "COUNT(*) AS totalVotes " +
            "from tb_voting_session tvs " +
            "join tb_vote tv on tv.id_voting_session = tvs.id " +
            "where tvs.id_external_uuid = :votingSessionId " ,
            nativeQuery = true)
    VotingSessionResult findVotingSessionResults(@Param("votingSessionId") UUID votingSessionId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE TB_VOTING_SESSION " +
            "SET VOTING_STATUS = 'CLOSED', " +
            "UPDATED_AT = :now " +
            "WHERE VOTING_STATUS = 'OPEN' " +
            "AND VOTING_END_TIME <= :now", nativeQuery = true)
    int closeExpiredSessions(@Param("now") OffsetDateTime now);
}
