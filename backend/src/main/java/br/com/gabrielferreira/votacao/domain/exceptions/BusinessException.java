package br.com.gabrielferreira.votacao.domain.exceptions;

import java.io.Serial;

public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6290839730857192546L;

    public BusinessException(String message) {
        super(message);
    }
}
