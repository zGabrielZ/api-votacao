package br.com.gabrielferreira.votacao.api.dtos.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record VotingSessionResultOutputDTO(
        @Schema(
                description = "Number of 'yes' votes",
                example = "10"
        )
        Long yesVotes,

        @Schema(
                description = "Number of 'no' votes",
                example = "5"
        )
        Long noVotes,

        @Schema(
                description = "Total number of votes",
                example = "15"
        )
        Long totalVotes
) implements Serializable {
}
