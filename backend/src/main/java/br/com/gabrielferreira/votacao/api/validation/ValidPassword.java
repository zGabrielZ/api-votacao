package br.com.gabrielferreira.votacao.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "{associate.password.strong}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
