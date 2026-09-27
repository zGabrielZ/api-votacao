package br.com.gabrielferreira.votacao.api.dtos.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record AgendaOutputDTO(
        @Schema(
                description = "Agenda ID",
                example = "123e4567-e89b-12d3-a456-426614174000"
        )
        UUID id,

        @Schema(
                description = "Agenda title",
                example = "Reunião de Planejamento"
        )
        String title,

        @Schema(
                description = "Agenda description",
                example = "Discussão sobre o plano de ação para o próximo trimestre"
        )
        String description
) implements Serializable {
}
