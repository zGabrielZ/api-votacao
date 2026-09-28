package br.com.gabrielferreira.votacao.domain.exceptions;

import java.io.Serial;
import java.util.UUID;

public class VotingSessionNotFoundException extends EntityNotFoundException {

    @Serial
    private static final long serialVersionUID = 6819374100959095306L;

    public VotingSessionNotFoundException(UUID votingSessionId) {
        super(String.format("Voting session not found with ID: %s", votingSessionId));
    }
}
