package br.com.gabrielferreira.votacao.api.mappers.associate.output;

import br.com.gabrielferreira.votacao.api.dtos.output.AssociateOutputDTO;
import br.com.gabrielferreira.votacao.api.dtos.output.page.PageResponse;
import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AssociateOutputMapper {

    AssociateOutputMapper INSTANCE = Mappers.getMapper(AssociateOutputMapper.class);

    @Mapping(source = "idExternalUuid", target = "id")
    @Mapping(source = "documentNumber", target = "documentNumber", qualifiedByName = "formatDocumentNumber")
    AssociateOutputDTO toAssociateOutputDTO(AssociateEntity associateEntity);

    default PageResponse<AssociateOutputDTO> toPageDto(Page<AssociateEntity> page) {
        if (CollectionUtils.isEmpty(page.getContent())) {
            return PageResponse.<AssociateOutputDTO>builder()
                    .content(Collections.emptyList())
                    .number((long) page.getNumber())
                    .size((long) page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages((long) page.getTotalPages())
                    .build();
        }

        List<AssociateOutputDTO> dtos = page.getContent().stream()
                .map(this::toAssociateOutputDTO)
                .toList();

        return PageResponse.<AssociateOutputDTO>builder()
                .content(dtos)
                .size((long) page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages((long) page.getTotalPages())
                .number((long) page.getNumber())
                .build();
    }

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
