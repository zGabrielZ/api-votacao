package br.com.gabrielferreira.votacao.api.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProblemDetailType {
    SYSTEM_ERROR("System Error", "system-error", "An internal system error has occurred. Please try again later."),
    BUSINESS_RULE_VIOLATION("Business Rule Violation", "business-rule-violation", "A business rule has been violated."),
    RESOURCE_NOT_FOUND("Resource Not Found", "resource-not-found", "The requested resource could not be found."),
    INVALID_DATA("Invalid Data", "invalid-data", "One or more fields are invalid. Please correct them and try again."),
    INVALID_PARAMETER("Invalid Parameter", "invalid-parameter", "One or more request parameters are invalid.");

    private final String title;
    private final String uri;
    private final String message;
}
