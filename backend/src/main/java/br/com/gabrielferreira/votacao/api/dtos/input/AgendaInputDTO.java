package br.com.gabrielferreira.votacao.api.dtos.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record AgendaInputDTO(
        @Schema(
                description = "Agenda title",
                example = "Reunião de Planejamento"
        )
        @NotBlank
        @Size(min = 1, max = 255)
        String title,

        @Schema(
                description = "Agenda description",
                example = "Discussão sobre o plano de ação para o próximo trimestre"
        )
        @Size(min = 1, max = 255)
        String description
) implements Serializable {
}
