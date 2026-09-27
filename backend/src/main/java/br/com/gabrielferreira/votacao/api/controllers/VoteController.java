package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.VoteInputDTO;
import br.com.gabrielferreira.votacao.api.mappers.vote.input.VoteInputMapper;
import br.com.gabrielferreira.votacao.domain.entities.VoteEntity;
import br.com.gabrielferreira.votacao.domain.services.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voting-sessions/{votingSessionId}/votes")
public class VoteController {

    private final VoteService voteService;
    private final VoteInputMapper voteInputMapper;

    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable UUID votingSessionId,
            @Valid @RequestBody VoteInputDTO voteInputDTO
    ) {
        VoteEntity voteEntity = voteInputMapper.toVoteEntity(voteInputDTO, votingSessionId);
        voteService.create(voteEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
