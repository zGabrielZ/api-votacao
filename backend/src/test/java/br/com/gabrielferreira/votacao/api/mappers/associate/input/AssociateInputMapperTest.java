package br.com.gabrielferreira.votacao.api.mappers.associate.input;

import br.com.gabrielferreira.votacao.api.dtos.input.AssociateInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
