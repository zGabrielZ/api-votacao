package br.com.gabrielferreira.votacao.api.mappers.vote.input;

import br.com.gabrielferreira.votacao.api.dtos.input.VoteInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.VoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VoteInputMapper {

    VoteInputMapper INSTANCE = Mappers.getMapper(VoteInputMapper.class);

    @Mapping(source = "voteInputDTO.associateId", target = "associate.idExternalUuid")
    @Mapping(source = "votingSessionId", target = "votingSession.idExternalUuid")
    @Mapping(source = "voteInputDTO.voteOption", target = "voteOption")
    VoteEntity toVoteEntity(VoteInputDTO voteInputDTO, UUID votingSessionId);
}
