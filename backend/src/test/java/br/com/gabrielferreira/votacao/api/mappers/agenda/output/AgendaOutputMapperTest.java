package br.com.gabrielferreira.votacao.api.mappers.agenda.output;

import br.com.gabrielferreira.votacao.api.dtos.output.AgendaOutputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for AgendaOutputMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AgendaOutputMapperTest {

    private final AgendaOutputMapper mapper = AgendaOutputMapper.INSTANCE;

    @Test
    @Order(1)
    void givenAgendaEntityWhenToDtoThenReturnAgendaOutputDTO() {
        UUID externalId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        AgendaEntity entity = AgendaEntity.builder()
                .id(1L)
                .idExternalUuid(externalId)
                .title("Title")
                .description("Description")
                .build();

        AgendaOutputDTO dto = mapper.toAgendaOutputDTO(entity);

        assertNotNull(dto);
        assertEquals(externalId, dto.id());
        assertEquals(entity.getTitle(), dto.title());
        assertEquals(entity.getDescription(), dto.description());
    }
}
