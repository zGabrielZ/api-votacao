package br.com.gabrielferreira.votacao.api.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record AgendaInputDTO(
        @NotBlank
        @Size(min = 1, max = 255)
        String title,

        @Size(min = 1, max = 255)
        String description
) implements Serializable {
}
