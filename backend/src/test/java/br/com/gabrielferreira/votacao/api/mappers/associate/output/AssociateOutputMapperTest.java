package br.com.gabrielferreira.votacao.api.mappers.associate.output;

import br.com.gabrielferreira.votacao.api.dtos.output.AssociateOutputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.page.PageResponse;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for AssociateOutputMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssociateOutputMapperTest {

    private final AssociateOutputMapper mapper = AssociateOutputMapper.INSTANCE;

    @Test
    @Order(1)
    void givenAssociateEntityWhenToDtoThenReturnAssociateOutputDTO() {
        UUID externalId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        AssociateEntity entity = AssociateEntity.builder()
                .id(1L)
                .idExternalUuid(externalId)
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .password("Password@123")
                .documentNumber("06738010044")
                .build();

        AssociateOutputDTO dto = mapper.toAssociateOutputDTO(entity);

        assertNotNull(dto);
        assertEquals(externalId, dto.id());
        assertEquals(entity.getName(), dto.name());
        assertEquals(entity.getEmail(), dto.email());
        assertEquals("067.380.100-44", dto.documentNumber());
    }

    @Test
    @Order(2)
    void givenPageOfAssociatesWhenToPageDtoThenReturnPageResponse() {
        UUID externalId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        AssociateEntity entity = AssociateEntity.builder()
                .id(1L)
                .idExternalUuid(externalId)
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .password("Password@123")
                .documentNumber("06738010044")
                .build();
        Page<AssociateEntity> page = new PageImpl<>(
                List.of(entity),
                PageRequest.of(1, 5),
                6
        );

        PageResponse<AssociateOutputDTO> result = mapper.toPageDto(page);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(externalId, result.getContent().get(0).id());
        assertEquals("Ana Costa", result.getContent().get(0).name());
        assertEquals("ana.costa@email.com", result.getContent().get(0).email());
        assertEquals("067.380.100-44", result.getContent().get(0).documentNumber());
        assertEquals(5L, result.getSize());
        assertEquals(6L, result.getTotalElements());
        assertEquals(2L, result.getTotalPages());
        assertEquals(1L, result.getNumber());
    }

    @Test
    @Order(3)
    void givenEmptyPageWhenToPageDtoThenReturnEmptyPageResponse() {
        Page<AssociateEntity> page = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 5),
                0
        );

        PageResponse<AssociateOutputDTO> result = mapper.toPageDto(page);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(0, result.getContent().size());
        assertEquals(5L, result.getSize());
        assertEquals(0L, result.getTotalElements());
        assertEquals(0L, result.getTotalPages());
        assertEquals(0L, result.getNumber());
    }
}
