package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.AgendaInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.AgendaOutputDTO;
import br.com.gabrielferreira.votacao.api.mappers.agenda.input.AgendaInputMapper;
import br.com.gabrielferreira.votacao.api.mappers.agenda.output.AgendaOutputMapper;
import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.services.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Agendas", description = "Agenda management endpoints")
@RestController
@RequestMapping("/v1/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService  agendaService;
    private final AgendaInputMapper agendaInputMapper;
    private final AgendaOutputMapper agendaOutputMapper;

    @Operation(summary = "Create a new agenda")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Agenda created successfully"
            )
    })
    @PostMapping
    public ResponseEntity<AgendaOutputDTO> create(
            @Valid @RequestBody AgendaInputDTO agendaInputDTO
    ) {
        AgendaEntity agendaEntity = agendaInputMapper.toAgendaEntity(agendaInputDTO);
        agendaEntity = agendaService.create(agendaEntity);

        AgendaOutputDTO agendaOutputDto = agendaOutputMapper.toAgendaOutputDTO(agendaEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agendaOutputDto);
    }
}
