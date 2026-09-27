package br.com.gabrielferreira.votacao.api.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record VoteInputDTO(
        @NotNull
        UUID associateId,

        @NotBlank
        String voteOption
) implements Serializable {
}
