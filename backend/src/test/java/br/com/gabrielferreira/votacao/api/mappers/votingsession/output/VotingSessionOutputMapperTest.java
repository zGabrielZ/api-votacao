package br.com.gabrielferreira.votacao.api.mappers.votingsession.output;

import br.com.gabrielferreira.votacao.api.dtos.output.VotingSessionOutputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.VotingSessionResultOutputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import br.com.gabrielferreira.votacao.domain.enums.VotingSessionStatus;
import br.com.gabrielferreira.votacao.domain.repositories.projection.VotingSessionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for VotingSessionOutputMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VotingSessionOutputMapperTest {

    private final VotingSessionOutputMapper mapper = VotingSessionOutputMapper.INSTANCE;

    @Test
    @Order(1)
    void givenVotingSessionEntityWhenToDtoThenReturnVotingSessionOutputDTO() {
        UUID externalId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        AgendaEntity agenda = AgendaEntity.builder().idExternalUuid(UUID.fromString("223e4567-e89b-12d3-a456-426614174111")).build();
        OffsetDateTime start = OffsetDateTime.parse("2026-01-01T10:00:00Z");
        OffsetDateTime end = OffsetDateTime.parse("2026-01-01T11:00:00Z");

        VotingSessionEntity entity = VotingSessionEntity.builder()
                .id(1L)
                .idExternalUuid(externalId)
                .agenda(agenda)
                .votingStartTime(start)
                .votingEndTime(end)
                .status(VotingSessionStatus.OPEN)
                .build();

        VotingSessionOutputDTO dto = mapper.toVotingSessionOutputDTO(entity);

        assertNotNull(dto);
        assertEquals(externalId, dto.id());
        assertEquals(start, dto.votingStartTime());
        assertEquals(end, dto.votingEndTime());
        assertEquals(VotingSessionStatus.OPEN.toString(), dto.votingStatus());
    }

    @Test
    @Order(2)
    void givenVotingSessionResultWhenToVotingSessionResultOutputDTOThenReturnDto() {
        VotingSessionResult result = new VotingSessionResult() {
            @Override
            public Long getYesVotes() {
                return 12L;
            }

            @Override
            public Long getNoVotes() {
                return 3L;
            }

            @Override
            public Long getTotalVotes() {
                return 15L;
            }
        };

        VotingSessionResultOutputDTO dto = mapper.toVotingSessionResultOutputDTO(result);

        assertNotNull(dto);
        assertEquals(12L, dto.yesVotes());
        assertEquals(3L, dto.noVotes());
        assertEquals(15L, dto.totalVotes());
    }
}
