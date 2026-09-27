package br.com.gabrielferreira.votacao.api.mappers.agenda.output;

import br.com.gabrielferreira.votacao.api.dtos.output.AgendaOutputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgendaOutputMapper {

    AgendaOutputMapper INSTANCE = Mappers.getMapper(AgendaOutputMapper.class);

    @Mapping(source = "idExternalUuid", target = "id")
    AgendaOutputDTO toAgendaOutputDTO(AgendaEntity agendaEntity);
}
