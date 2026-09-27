package br.com.gabrielferreira.votacao.domain.entities;

import br.com.gabrielferreira.votacao.domain.enums.VotingSessionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_VOTING_SESSION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VotingSessionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7804972098282537815L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ID_EXTERNAL_UUID", nullable = false, unique = true, updatable = false)
    private UUID idExternalUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AGENDA", nullable = false)
    private AgendaEntity agenda;

    @Column(name = "VOTING_START_TIME", nullable = false)
    private OffsetDateTime votingStartTime;

    @Column(name = "VOTING_END_TIME", nullable = false)
    private OffsetDateTime votingEndTime;

    @Column(name = "VOTING_STATUS", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private VotingSessionStatus status;

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
        if (this.status == null) {
            this.status = VotingSessionStatus.OPEN;
        }
    }
}
