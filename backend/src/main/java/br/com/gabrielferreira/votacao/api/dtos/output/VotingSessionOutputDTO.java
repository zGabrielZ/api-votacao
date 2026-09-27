package br.com.gabrielferreira.votacao.api.dtos.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record VotingSessionOutputDTO(
        @Schema(
                description = "Voting session identifier",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "Voting session start time",
                example = "2023-01-01T00:00:00Z"
        )
        OffsetDateTime votingStartTime,

        @Schema(
                description = "Voting session end time",
                example = "2023-01-01T01:00:00Z"
        )
        OffsetDateTime votingEndTime,

        @Schema(
                description = "Voting session status",
                example = "OPEN"
        )
        String votingStatus
) implements Serializable {
}
