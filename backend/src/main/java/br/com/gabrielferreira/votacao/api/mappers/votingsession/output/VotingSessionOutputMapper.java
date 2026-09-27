package br.com.gabrielferreira.votacao.api.mappers.votingsession.output;

import br.com.gabrielferreira.votacao.api.dtos.output.VotingSessionOutputDTO;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VotingSessionOutputMapper {

    VotingSessionOutputMapper INSTANCE = Mappers.getMapper(VotingSessionOutputMapper.class);

    @Mapping(source = "idExternalUuid", target = "id")
    @Mapping(source = "status", target = "votingStatus")
    VotingSessionOutputDTO toVotingSessionOutputDTO(VotingSessionEntity entity);
}
