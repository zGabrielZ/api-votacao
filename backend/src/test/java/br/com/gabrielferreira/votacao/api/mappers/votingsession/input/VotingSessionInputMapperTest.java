package br.com.gabrielferreira.votacao.api.mappers.votingsession.input;

import br.com.gabrielferreira.votacao.api.dtos.input.VotingSessionInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for VotingSessionInputMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VotingSessionInputMapperTest {

    private final VotingSessionInputMapper mapper = VotingSessionInputMapper.INSTANCE;

    @Test
    @Order(1)
    void givenVotingSessionInputDTOWhenToEntityThenReturnVotingSessionEntity() {
        UUID agendaExternalId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        OffsetDateTime start = OffsetDateTime.parse("2026-01-01T10:00:00Z");
        OffsetDateTime end = OffsetDateTime.parse("2026-01-01T11:00:00Z");

        VotingSessionInputDTO dto = VotingSessionInputDTO.builder()
                .votingStartTime(start)
                .votingEndTime(end)
                .build();

        VotingSessionEntity entity = mapper.toVotingSessionEntity(dto, agendaExternalId);

        assertNotNull(entity);
        assertNotNull(entity.getAgenda());
        assertEquals(agendaExternalId, entity.getAgenda().getIdExternalUuid());
        assertEquals(start, entity.getVotingStartTime());
        assertEquals(end, entity.getVotingEndTime());
    }
}
