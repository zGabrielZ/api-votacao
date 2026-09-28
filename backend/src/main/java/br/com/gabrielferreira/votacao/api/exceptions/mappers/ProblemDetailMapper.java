package br.com.gabrielferreira.votacao.api.exceptions.mappers;

import br.com.gabrielferreira.votacao.api.exceptions.dtos.ProblemDetailDTO;
import br.com.gabrielferreira.votacao.api.exceptions.dtos.ProblemDetailFieldDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProblemDetailMapper {

    ProblemDetailMapper INSTANCE = Mappers.getMapper(ProblemDetailMapper.class);

    ProblemDetailDTO toProblemDetailDto(
            Integer status,
            String title,
            String detail,
            String message,
            OffsetDateTime timestamp,
            List<ProblemDetailFieldDTO> fields
    );

    ProblemDetailFieldDTO toProblemDetailFieldDto(String field, String message);
}
