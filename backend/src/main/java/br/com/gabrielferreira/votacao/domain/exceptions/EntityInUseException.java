package br.com.gabrielferreira.votacao.domain.exceptions;

import java.io.Serial;

public class EntityInUseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1234567890123456789L;

    public EntityInUseException(String message) {
        super(message);
    }
}
