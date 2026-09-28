package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.VoteInputDTO;
import br.com.gabrielferreira.votacao.api.mappers.vote.input.VoteInputMapper;
import br.com.gabrielferreira.votacao.domain.entities.VoteEntity;
import br.com.gabrielferreira.votacao.domain.services.VoteService;
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

@Tag(name = "Votes", description = "Vote management endpoints")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/voting-sessions/{votingSessionId}/votes")
public class VoteController {

    private final VoteService voteService;
    private final VoteInputMapper voteInputMapper;

    @Operation(summary = "Create a new vote")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vote created successfully"
            )
    })
    @PostMapping
    public ResponseEntity<Void> create(
            @Parameter(
                    description = "Voting session identifier",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                    required = true
            )
            @PathVariable UUID votingSessionId,
            @Valid @RequestBody VoteInputDTO voteInputDTO
    ) {
        VoteEntity voteEntity = voteInputMapper.toVoteEntity(voteInputDTO, votingSessionId);
        voteService.create(voteEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
