package br.com.gabrielferreira.votacao.api.mappers.associate.output;

import br.com.gabrielferreira.votacao.api.dtos.output.AssociateOutputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
}
