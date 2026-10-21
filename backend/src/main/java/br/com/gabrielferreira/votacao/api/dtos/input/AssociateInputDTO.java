package br.com.gabrielferreira.votacao.api.dtos.input;

import io.swagger.v3.oas.annotations.media.Schema;
import br.com.gabrielferreira.votacao.api.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@Builder
public record AssociateInputDTO(
        @Schema(
                description = "Associate full name",
                example = "Ana Costa"
        )
        @NotBlank
        @Size(min = 1, max = 255)
        String name,

        @Schema(
                description = "Associate email",
                example = "ana.costa@email.com"
        )
        @NotBlank
        @Email
        // The regex pattern is added to ensure that the email follows a valid format, including a domain and a top-level domain.
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "{associate.email.invalid}")
        String email,

        @Schema(
                description = "Associate password",
                example = "Password@123"
        )
        @NotBlank
        @Size(min = 1, max = 255)
        @ValidPassword
        String password,

        @Schema(
                description = "Associate document number (CPF)",
                example = "06738010044"
        )
        @NotBlank
        @CPF
        String documentNumber
) implements Serializable {
}
