package br.com.gabrielferreira.votacao.api.dtos.output;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record AgendaOutputDTO(
        UUID id,

        String title,

        String description
) implements Serializable {
}
