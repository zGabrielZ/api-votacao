package br.com.gabrielferreira.votacao.domain.entities;

import br.com.gabrielferreira.votacao.domain.enums.VoteOption;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_VOTE",
        uniqueConstraints = @UniqueConstraint(name = "tb_vote_voting_session_unique", columnNames = {"ID_VOTING_SESSION", "ID_ASSOCIATE"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VoteEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3552069673041396679L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ID_EXTERNAL_UUID", nullable = false, unique = true, updatable = false)
    private UUID idExternalUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOTING_SESSION", nullable = false)
    private VotingSessionEntity votingSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ASSOCIATE", nullable = false)
    private AssociateEntity associate;

    @Column(name = "VOTE_OPTION", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private VoteOption voteOption;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.idExternalUuid == null) {
            this.idExternalUuid = UUID.randomUUID();
        }
    }
}
