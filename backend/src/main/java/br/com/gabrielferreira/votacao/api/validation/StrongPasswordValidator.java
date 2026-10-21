package br.com.gabrielferreira.votacao.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        if (value.length() < 8) {
            return false;
        }

        boolean hasUppercase = value.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = value.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = value.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = value.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        return hasUppercase && hasLowercase && hasDigit && hasSpecial;
    }
}
