package br.com.gabrielferreira.votacao.api.dtos.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record AssociateOutputDTO(
        @Schema(
                description = "Associate ID",
                example = "123e4567-e89b-12d3-a456-426614174000"
        )
        UUID id,

        @Schema(
                description = "Associate full name",
                example = "Ana Costa"
        )
        String name,

        @Schema(
                description = "Associate email",
                example = "ana.costa@email.com"
        )
        String email,

        @Schema(
                description = "Associate document number (CPF)",
                example = "06738010044"
        )
        String documentNumber
) implements Serializable {
}
