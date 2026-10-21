package br.com.gabrielferreira.votacao.api.mappers.associate.output;

import br.com.gabrielferreira.votacao.api.dtos.output.AssociateOutputDTO;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssociateOutputMapper {

    AssociateOutputMapper INSTANCE = Mappers.getMapper(AssociateOutputMapper.class);

    @Mapping(source = "idExternalUuid", target = "id")
    @Mapping(source = "documentNumber", target = "documentNumber", qualifiedByName = "formatDocumentNumber")
    AssociateOutputDTO toAssociateOutputDTO(AssociateEntity associateEntity);

    @Named("formatDocumentNumber")
    default String formatDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            return null;
        }

        String digits = documentNumber.replaceAll("\\D+", "");
        if (digits.length() != 11) {
            return digits;
        }

        return digits.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
