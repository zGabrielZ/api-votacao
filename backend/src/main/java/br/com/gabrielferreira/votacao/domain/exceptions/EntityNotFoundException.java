package br.com.gabrielferreira.votacao.domain.exceptions;

import java.io.Serial;

public abstract class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2140334976529380860L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
