package br.com.gabrielferreira.votacao.api.exceptions.mappers;

import br.com.gabrielferreira.votacao.api.exceptions.dtos.ProblemDetailDTO;
import br.com.gabrielferreira.votacao.api.exceptions.dtos.ProblemDetailFieldDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for ProblemDetailMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProblemDetailMapperTest {

    private final ProblemDetailMapper mapper = ProblemDetailMapper.INSTANCE;

    @Test
    @Order(1)
    void givenParametersWhenToProblemDetailDtoThenReturnMappedDto() {
        Integer status = 400;
        String title = "Invalid Data";
        String detail = "One or more fields are invalid.";
        String message = "Validation failed.";
        OffsetDateTime timestamp = OffsetDateTime.parse("2026-01-01T10:00:00Z");
        ProblemDetailFieldDTO field = ProblemDetailFieldDTO.builder()
                .field("email")
                .message("The email format is invalid.")
                .build();

        ProblemDetailDTO dto = mapper.toProblemDetailDto(status, title, detail, message, timestamp, List.of(field));

        assertNotNull(dto);
        assertEquals(status, dto.status());
        assertEquals(title, dto.title());
        assertEquals(detail, dto.detail());
        assertEquals(message, dto.message());
        assertEquals(timestamp, dto.timestamp());
        assertNotNull(dto.fields());
        assertEquals(1, dto.fields().size());
        assertEquals("email", dto.fields().get(0).field());
        assertEquals("The email format is invalid.", dto.fields().get(0).message());
    }

    @Test
    @Order(2)
    void givenFieldWhenToProblemDetailFieldDtoThenReturnMappedFieldDto() {
        ProblemDetailFieldDTO dto = mapper.toProblemDetailFieldDto("name", "Name is required.");

        assertNotNull(dto);
        assertEquals("name", dto.field());
        assertEquals("Name is required.", dto.message());
    }
}
