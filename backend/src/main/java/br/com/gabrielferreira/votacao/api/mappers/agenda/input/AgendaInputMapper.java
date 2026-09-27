package br.com.gabrielferreira.votacao.api.mappers.agenda.input;

import br.com.gabrielferreira.votacao.api.dtos.input.AgendaInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AgendaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AgendaInputMapper {

    AgendaInputMapper INSTANCE = Mappers.getMapper(AgendaInputMapper.class);

    AgendaEntity toAgendaEntity(AgendaInputDTO agendaInputDto);
}
