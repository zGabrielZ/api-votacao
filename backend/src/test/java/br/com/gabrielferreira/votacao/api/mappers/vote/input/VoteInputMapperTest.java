package br.com.gabrielferreira.votacao.api.mappers.vote.input;

import br.com.gabrielferreira.votacao.api.dtos.input.VoteInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.VoteEntity;
import br.com.gabrielferreira.votacao.domain.enums.VoteOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for VoteInputMapper")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VoteInputMapperTest {

    private final VoteInputMapper mapper = VoteInputMapper.INSTANCE;

    @Test
    @Order(1)
    void givenVoteInputDTOWhenToEntityThenReturnVoteEntity() {
        UUID associateId = UUID.fromString("11111111-1111-4111-8111-111111111111");
        UUID votingSessionId = UUID.fromString("22222222-2222-4222-8222-222222222222");

        VoteInputDTO dto = VoteInputDTO.builder()
                .associateId(associateId)
                .voteOption("YES")
                .build();

        VoteEntity entity = mapper.toVoteEntity(dto, votingSessionId);

        assertNotNull(entity);
        assertNotNull(entity.getAssociate());
        assertNotNull(entity.getVotingSession());
        assertEquals(associateId, entity.getAssociate().getIdExternalUuid());
        assertEquals(votingSessionId, entity.getVotingSession().getIdExternalUuid());
        assertEquals(VoteOption.YES, entity.getVoteOption());
    }
}
