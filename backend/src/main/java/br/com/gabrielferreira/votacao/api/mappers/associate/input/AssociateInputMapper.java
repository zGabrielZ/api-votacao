package br.com.gabrielferreira.votacao.api.mappers.associate.input;

import br.com.gabrielferreira.votacao.api.dtos.input.AssociateInputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssociateInputMapper {

    AssociateInputMapper INSTANCE = Mappers.getMapper(AssociateInputMapper.class);

    @Mapping(target = "documentNumber", expression = "java(cleanDocumentNumber(associateInputDto.documentNumber()))")
    AssociateEntity toAssociateEntity(AssociateInputDTO associateInputDto);

    @Named("cleanDocumentNumber")
    default String cleanDocumentNumber(String documentNumber) {
        if (documentNumber == null) {
            return null;
        }
        return documentNumber.replaceAll("\\D+", "");
    }
}
