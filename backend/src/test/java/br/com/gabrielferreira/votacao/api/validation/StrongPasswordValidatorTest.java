package br.com.gabrielferreira.votacao.api.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for StrongPasswordValidator")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StrongPasswordValidatorTest {

    private final StrongPasswordValidator validator = new StrongPasswordValidator();

    @Test
    @Order(1)
    void givenStrongPasswordWhenIsValidThenReturnTrue() {
        assertTrue(validator.isValid("Password@123", null));
    }

    @Test
    @Order(2)
    void givenPasswordWithoutSpecialCharacterWhenIsValidThenReturnFalse() {
        assertFalse(validator.isValid("Password123", null));
    }

    @Test
    @Order(3)
    void givenPasswordWithoutUppercaseWhenIsValidThenReturnFalse() {
        assertFalse(validator.isValid("password@123", null));
    }

    @Test
    @Order(4)
    void givenPasswordWithoutLowercaseWhenIsValidThenReturnFalse() {
        assertFalse(validator.isValid("PASSWORD@123", null));
    }

    @Test
    @Order(5)
    void givenPasswordWithoutDigitWhenIsValidThenReturnFalse() {
        assertFalse(validator.isValid("Password@abc", null));
    }

    @Test
    @Order(6)
    void givenPasswordTooShortWhenIsValidThenReturnFalse() {
        assertFalse(validator.isValid("Ab1@", null));
    }
}
