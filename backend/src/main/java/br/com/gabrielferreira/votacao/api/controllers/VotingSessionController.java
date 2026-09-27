package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.VotingSessionInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.VotingSessionOutputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.VotingSessionResultOutputDTO;
import br.com.gabrielferreira.votacao.api.mappers.votingsession.input.VotingSessionInputMapper;
import br.com.gabrielferreira.votacao.api.mappers.votingsession.output.VotingSessionOutputMapper;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.repositories.projection.VotingSessionResult;
import br.com.gabrielferreira.votacao.domain.services.VotingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VotingSessionController {

    private final VotingSessionService votingSessionService;
    private final VotingSessionInputMapper votingSessionInputMapper;
    private final VotingSessionOutputMapper votingSessionOutputMapper;

    @PostMapping("/v1/agenda/{agendaId}/voting-sessions")
    public ResponseEntity<VotingSessionOutputDTO> create(
            @PathVariable UUID agendaId,
            @Valid @RequestBody VotingSessionInputDTO votingSessionInputDTO
    ) {
        VotingSessionEntity entity = votingSessionInputMapper.toVotingSessionEntity(votingSessionInputDTO, agendaId);
        entity = votingSessionService.create(entity);
        VotingSessionOutputDTO dto = votingSessionOutputMapper.toVotingSessionOutputDTO(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/v1/voting-sessions/{votingSessionId}/results")
    public ResponseEntity<VotingSessionResultOutputDTO> getVotingSessionResults(
            @PathVariable UUID votingSessionId
    ) {
        VotingSessionResult result = votingSessionService.getVotingSessionResults(votingSessionId);
        VotingSessionResultOutputDTO dto = votingSessionOutputMapper.toVotingSessionResultOutputDTO(result);
        return ResponseEntity.ok(dto);
    }
}
