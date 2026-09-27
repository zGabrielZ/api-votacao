package br.com.gabrielferreira.votacao.domain.exceptions;

import java.io.Serial;
import java.util.UUID;

public class AgendaNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2978653908409031146L;

    public AgendaNotFoundException(UUID agendaId) {
        super(String.format("Agenda not found with ID: %s", agendaId));
    }
}
