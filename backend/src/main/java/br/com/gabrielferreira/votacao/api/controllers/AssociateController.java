package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.AssociateInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.AssociateOutputDTO;
import br.com.gabrielferreira.votacao.api.mappers.associate.input.AssociateInputMapper;
import br.com.gabrielferreira.votacao.api.mappers.associate.output.AssociateOutputMapper;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.services.AssociateService;
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

@Tag(name = "Associates", description = "Associate management endpoints")
@RestController
@RequestMapping("/v1/associates")
@RequiredArgsConstructor
public class AssociateController {

    private final AssociateService associateService;
    private final AssociateInputMapper associateInputMapper;
    private final AssociateOutputMapper associateOutputMapper;

    @Operation(summary = "Create a new associate")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Associate created successfully"
            )
    })
    @PostMapping
    public ResponseEntity<AssociateOutputDTO> create(
            @Valid @RequestBody AssociateInputDTO associateInputDTO
    ) {
        AssociateEntity associateEntity = associateInputMapper.toAssociateEntity(associateInputDTO);
        associateEntity = associateService.create(associateEntity);

        AssociateOutputDTO associateOutputDto = associateOutputMapper.toAssociateOutputDTO(associateEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(associateOutputDto);
    }
}
