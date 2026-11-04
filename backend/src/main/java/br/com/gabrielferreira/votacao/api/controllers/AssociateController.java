package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.filter.AssociateFilterDTO;
import br.com.gabrielferreira.votacao.api.dtos.input.AssociateInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.input.UpdateAssociateInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.AssociateOutputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.page.PageResponse;
import br.com.gabrielferreira.votacao.api.mappers.associate.input.AssociateInputMapper;
import br.com.gabrielferreira.votacao.api.mappers.associate.output.AssociateOutputMapper;
import br.com.gabrielferreira.votacao.core.utils.PageTranslate;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.repositories.filter.associate.AssociateFilter;
import br.com.gabrielferreira.votacao.domain.services.AssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @Operation(summary = "Get associate by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Associate found successfully"
            )
    })
    @GetMapping("/{associateId}")
    public ResponseEntity<AssociateOutputDTO> findById(
            @PathVariable UUID associateId
    ) {
        AssociateEntity associateEntity = associateService.findById(associateId);
        AssociateOutputDTO associateOutputDto = associateOutputMapper.toAssociateOutputDTO(associateEntity);
        return ResponseEntity.ok(associateOutputDto);
    }

    @Operation(summary = "Update an associate")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Associate updated successfully"
            )
    })
    @PutMapping("/{associateId}")
    public ResponseEntity<AssociateOutputDTO> update(
            @PathVariable UUID associateId,
            @Valid @RequestBody UpdateAssociateInputDTO updateAssociateInputDTO
    ) {
        AssociateEntity associateEntity = associateInputMapper.toAssociateEntity(updateAssociateInputDTO);
        associateEntity = associateService.update(associateId, associateEntity);
        AssociateOutputDTO associateOutputDto = associateOutputMapper.toAssociateOutputDTO(associateEntity);
        return ResponseEntity.ok(associateOutputDto);
    }

    @Operation(summary = "Delete an associate")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Associate deleted successfully"
            )
    })
    @DeleteMapping("/{associateId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID associateId
    ) {
        associateService.delete(associateId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search associates with filters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Associates found successfully"
            )
    })
    @PostMapping("/search")
    public ResponseEntity<PageResponse<AssociateOutputDTO>> search(
            @ParameterObject @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            @Valid @RequestBody AssociateFilterDTO filter
    ) {
        pageable = PageTranslate.toPageable(pageable, PageTranslate.getAssociatePageableFieldsMapping());
        AssociateFilter associateFilter = associateInputMapper.toAssociateFilter(filter);
        Page<AssociateEntity> associates = associateService.findAll(associateFilter, pageable);

        PageResponse<AssociateOutputDTO> pageResponse = associateOutputMapper.toPageDto(associates);
        return ResponseEntity.ok(pageResponse);
    }
}
