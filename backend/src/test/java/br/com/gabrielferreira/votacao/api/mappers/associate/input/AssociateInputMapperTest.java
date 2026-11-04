package br.com.gabrielferreira.votacao.api.mappers.associate.input;

import br.com.gabrielferreira.votacao.api.dtos.filter.AssociateFilterDTO;
import br.com.gabrielferreira.votacao.api.dtos.input.AssociateInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.input.UpdateAssociateInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.repositories.filter.associate.AssociateFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Tests for AssociateInputMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssociateInputMapperTest {

    private final AssociateInputMapper mapper = AssociateInputMapper.INSTANCE;

    @Test
    @Order(1)
    void givenAssociateInputDTOWhenToEntityThenReturnAssociateEntity() {
        AssociateInputDTO dto = AssociateInputDTO.builder()
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .password("Password@123")
                .documentNumber("067.380.100-44")
                .build();

        AssociateEntity result = mapper.toAssociateEntity(dto);

        assertNotNull(result);
        assertEquals(dto.name(), result.getName());
        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.password(), result.getPassword());
        assertEquals("06738010044", result.getDocumentNumber());
    }

    @Test
    @Order(2)
    void givenUpdateAssociateInputDTOWhenToEntityThenReturnAssociateEntity() {
        UpdateAssociateInputDTO dto = UpdateAssociateInputDTO.builder()
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .password("Password@123")
                .build();

        AssociateEntity result = mapper.toAssociateEntity(dto);

        assertNotNull(result);
        assertEquals(dto.name(), result.getName());
        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.password(), result.getPassword());
    }

    @Test
    @Order(3)
    void givenAssociateFilterDTOWhenToAssociateFilterThenReturnAssociateFilter() {
        UUID associateId = UUID.randomUUID();
        AssociateFilterDTO dto = AssociateFilterDTO.builder()
                .associateId(associateId)
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .documentNumber("06738010044")
                .build();

        AssociateFilter result = mapper.toAssociateFilter(dto);

        assertNotNull(result);
        assertEquals(associateId, result.associateId());
        assertEquals("Ana Costa", result.name());
        assertEquals("ana.costa@email.com", result.email());
        assertEquals("06738010044", result.documentNumber());
    }

    @Test
    @Order(4)
    void givenEmptyAssociateFilterDTOWhenToAssociateFilterThenReturnEmptyAssociateFilter() {
        AssociateFilterDTO dto = AssociateFilterDTO.builder().build();

        AssociateFilter result = mapper.toAssociateFilter(dto);

        assertNotNull(result);
        assertNull(result.associateId());
        assertNull(result.name());
        assertNull(result.email());
        assertNull(result.documentNumber());
    }
}
