package br.com.gabrielferreira.votacao.api.mappers.agenda.input;

import br.com.gabrielferreira.votacao.api.dtos.input.AgendaInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for AgendaInputMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AgendaInputMapperTest {

    private final AgendaInputMapper mapper = AgendaInputMapper.INSTANCE;

    @Test
    @Order(1)
    void givenAgendaInputDTOWhenToEntityThenReturnAgendaEntity() {
        AgendaInputDTO dto = AgendaInputDTO.builder()
                .title("Title")
                .description("Description")
                .build();

        AgendaEntity result = mapper.toAgendaEntity(dto);

        assertNotNull(result);
        assertEquals(dto.title(), result.getTitle());
        assertEquals(dto.description(), result.getDescription());
    }
}
