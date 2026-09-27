package br.com.gabrielferreira.votacao.api.dtos.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
public record VotingSessionInputDTO(
        @Schema(
                description = "Voting session start time",
                example = "2023-01-01T00:00:00Z"
        )
        @NotNull
        @FutureOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime votingStartTime,

        @Schema(
                description = "Voting session end time",
                example = "2023-01-01T01:00:00Z"
        )
        @NotNull
        @FutureOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime votingEndTime
) implements Serializable {
}
