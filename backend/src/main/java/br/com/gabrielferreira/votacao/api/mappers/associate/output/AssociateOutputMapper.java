package br.com.gabrielferreira.votacao.api.mappers.associate.output;

import br.com.gabrielferreira.votacao.api.dtos.output.AssociateOutputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssociateOutputMapper {

    AssociateOutputMapper INSTANCE = Mappers.getMapper(AssociateOutputMapper.class);

    @Mapping(source = "idExternalUuid", target = "id")
    AssociateOutputDTO toAssociateOutputDTO(AssociateEntity associateEntity);
}
