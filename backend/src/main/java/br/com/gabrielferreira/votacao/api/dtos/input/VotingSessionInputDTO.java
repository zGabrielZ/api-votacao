package br.com.gabrielferreira.votacao.api.dtos.input;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
public record VotingSessionInputDTO(
        @NotNull
        @FutureOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime votingStartTime,

        @NotNull
        @FutureOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime votingEndTime
) implements Serializable {
}
