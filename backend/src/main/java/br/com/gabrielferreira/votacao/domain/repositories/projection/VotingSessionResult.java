package br.com.gabrielferreira.votacao.domain.repositories.projection;

public interface VotingSessionResult {

    Long getYesVotes();

    Long getNoVotes();

    Long getTotalVotes();
}
