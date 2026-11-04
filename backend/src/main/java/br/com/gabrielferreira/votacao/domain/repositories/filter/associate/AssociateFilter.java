package br.com.gabrielferreira.votacao.domain.repositories.filter.associate;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Builder
public record AssociateFilter(
        UUID associateId,
        String name,
        String email,
        String documentNumber
) implements Serializable {

    public boolean hasAssociateId() {
        return Objects.nonNull(this.associateId);
    }

    public boolean hasName() {
        return StringUtils.isNotBlank(this.name);
    }

    public boolean hasEmail() {
        return StringUtils.isNotBlank(this.email);
    }

    public boolean hasDocumentNumber() {
        return StringUtils.isNotBlank(this.documentNumber);
    }
}
