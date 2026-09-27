package br.com.gabrielferreira.votacao.domain.exceptions;

import java.io.Serial;
import java.util.UUID;

public class AssociateNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7215532819342419932L;

    public AssociateNotFoundException(UUID associateId) {
        super(String.format("Associate not found with ID: %s", associateId));
    }
}
