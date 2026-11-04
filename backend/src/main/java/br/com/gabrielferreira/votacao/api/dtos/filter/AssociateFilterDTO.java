package br.com.gabrielferreira.votacao.api.dtos.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record AssociateFilterDTO(
        @Schema(
                description = "Associate external identifier",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID associateId,

        @Schema(
                description = "Associate name",
                example = "John"
        )
        String name,

        @Schema(
                description = "Associate email",
                example = "john@email.com"
        )
        String email,

        @Schema(
                description = "Associate document number",
                example = "12345678900"
        )
        String documentNumber
) implements Serializable {
}
