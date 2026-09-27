package br.com.gabrielferreira.votacao.api.mappers.votingsession.input;

import br.com.gabrielferreira.votacao.api.dtos.input.VotingSessionInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.VotingSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VotingSessionInputMapper {

    VotingSessionInputMapper INSTANCE = Mappers.getMapper(VotingSessionInputMapper.class);

    @Mapping(source = "agendaId", target = "agenda.idExternalUuid")
    VotingSessionEntity toVotingSessionEntity(VotingSessionInputDTO inputDTO, UUID agendaId);
}
