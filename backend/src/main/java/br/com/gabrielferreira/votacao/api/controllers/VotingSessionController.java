package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.VotingSessionInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.VotingSessionOutputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.VotingSessionResultOutputDTO;
import br.com.gabrielferreira.votacao.api.mappers.votingsession.input.VotingSessionInputMapper;
import br.com.gabrielferreira.votacao.api.mappers.votingsession.output.VotingSessionOutputMapper;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.repositories.projection.VotingSessionResult;
import br.com.gabrielferreira.votacao.domain.services.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Voting Sessions", description = "Voting session management endpoints")
@RestController
@RequiredArgsConstructor
public class VotingSessionController {

    private final VotingSessionService votingSessionService;
    private final VotingSessionInputMapper votingSessionInputMapper;
    private final VotingSessionOutputMapper votingSessionOutputMapper;

    @Operation(summary = "Create a new voting session")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Voting session created successfully"
            )
    })
    @PostMapping("/v1/agenda/{agendaId}/voting-sessions")
    public ResponseEntity<VotingSessionOutputDTO> create(
            @Parameter(
                    description = "Agenda identifier",
                    example = "55c318b4-685d-4920-b769-a9eecb876bba",
                    required = true
            )
            @PathVariable UUID agendaId,
            @Valid @RequestBody VotingSessionInputDTO votingSessionInputDTO
    ) {
        VotingSessionEntity entity = votingSessionInputMapper.toVotingSessionEntity(votingSessionInputDTO, agendaId);
        entity = votingSessionService.create(entity);
        VotingSessionOutputDTO dto = votingSessionOutputMapper.toVotingSessionOutputDTO(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get voting session results")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voting session results retrieved successfully"
            )
    })
    @GetMapping("/v1/voting-sessions/{votingSessionId}/results")
    public ResponseEntity<VotingSessionResultOutputDTO> getVotingSessionResults(
            @Parameter(
                    description = "Voting session identifier",
                    example = "80354266-cbab-4a83-a233-6e0d3c158e6d",
                    required = true
            )
            @PathVariable UUID votingSessionId
    ) {
        VotingSessionResult result = votingSessionService.getVotingSessionResults(votingSessionId);
        VotingSessionResultOutputDTO dto = votingSessionOutputMapper.toVotingSessionResultOutputDTO(result);
        return ResponseEntity.ok(dto);
    }
}
