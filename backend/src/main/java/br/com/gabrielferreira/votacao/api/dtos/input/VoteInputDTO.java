package br.com.gabrielferreira.votacao.api.dtos.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record VoteInputDTO(
        @Schema(
                description = "Associate identifier",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        @NotNull
        UUID associateId,

        @Schema(
                description = "Vote option. Allowed values: YES or NO",
                example = "YES",
                allowableValues = {"YES", "NO"}
        )
        @NotBlank
        String voteOption
) implements Serializable {
}
