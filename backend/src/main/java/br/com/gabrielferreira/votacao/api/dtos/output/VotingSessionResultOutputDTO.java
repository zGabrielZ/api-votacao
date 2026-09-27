package br.com.gabrielferreira.votacao.api.dtos.output;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record VotingSessionResultOutputDTO(
        Long yesVotes,

        Long noVotes,

        Long totalVotes
) implements Serializable {
}
