package br.com.gabrielferreira.votacao.api.dtos.output;

import lombok.Builder;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record VotingSessionOutputDTO(
        UUID id,

        OffsetDateTime votingStartTime,

        OffsetDateTime votingEndTime,

        String votingStatus
) implements Serializable {
}
