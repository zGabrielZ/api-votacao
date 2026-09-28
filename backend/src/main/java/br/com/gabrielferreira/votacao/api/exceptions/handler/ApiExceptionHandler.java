package br.com.gabrielferreira.votacao.api.exceptions.handler;

import br.com.gabrielferreira.votacao.api.exceptions.dtos.ProblemDetailDTO;
import br.com.gabrielferreira.votacao.api.exceptions.dtos.ProblemDetailFieldDTO;
import br.com.gabrielferreira.votacao.api.exceptions.enums.ProblemDetailType;
import br.com.gabrielferreira.votacao.api.exceptions.mappers.ProblemDetailMapper;
import br.com.gabrielferreira.votacao.domain.exceptions.BusinessException;
import br.com.gabrielferreira.votacao.domain.exceptions.EntityNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String GENERIC_USER_MESSAGE = "An unexpected internal system error has occurred. Please try again and if the problem persists, contact the system administrator.";
    private static final OffsetDateTime NOW = OffsetDateTime.now(ZoneOffset.UTC);

    private final MessageSource messageSource;

    private final ProblemDetailMapper problemDetailMapper;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "System Error",
                                    value = """
                                            {
                                              "status": 500,
                                              "title": "System Error",
                                              "detail": "An unexpected internal system error has occurred. Please try again and if the problem persists, contact the system administrator.",
                                              "message": "An unexpected internal system error has occurred. Please try again and if the problem persists, contact the system administrator.",
                                              "timestamp": "2024-06-01T12:00:00Z"
                                            }
                                            """
                            )
                    }
            )
    )
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetailType problemDetailType = ProblemDetailType.SYSTEM_ERROR;
        ProblemDetailDTO problemDetailDto = createProblemDetailDto(
                httpStatus,
                problemDetailType,
                GENERIC_USER_MESSAGE,
                null
        );
        return handleExceptionInternal(ex, problemDetailDto, new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Business Rule Violation",
                                    value = """
                                            {
                                              "status": 400,
                                              "title": "Business Rule Violation",
                                              "detail": "The operation cannot be completed due to business rule violation.",
                                              "message": "The operation cannot be completed due to business rule violation.",
                                              "timestamp": "2024-06-01T12:00:00Z"
                                            }
                                            """
                            )
                    }
            )
    )
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        log.warn(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ProblemDetailType problemDetailType = ProblemDetailType.BUSINESS_RULE_VIOLATION;
        ProblemDetailDTO problemDetailDto = createProblemDetailDto(
                httpStatus,
                problemDetailType,
                ex.getMessage(),
                null
        );
        return handleExceptionInternal(ex, problemDetailDto, new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Entity Not Found",
                                    value = """
                                            {
                                              "status": 404,
                                              "title": "Resource Not Found",
                                              "detail": "The requested resource was not found.",
                                              "message": "The requested resource was not found.",
                                              "timestamp": "2024-06-01T12:00:00Z"
                                            }
                                            """
                            )
                    }
            )
    )
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.warn(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ProblemDetailType problemDetailType = ProblemDetailType.RESOURCE_NOT_FOUND;
        ProblemDetailDTO problemDetailDto = createProblemDetailDto(
                httpStatus,
                problemDetailType,
                ex.getMessage(),
                null
        );
        return handleExceptionInternal(ex, problemDetailDto, new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.warn(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ProblemDetailType problemDetailType = ProblemDetailType.INVALID_PARAMETER;

        String detail = ex.getMessage();
        if (detail != null && detail.contains("No enum constant")) {
            detail = "The request contains an invalid value. Please verify the fields and try again.";
        } else if (detail == null || detail.isBlank()) {
            detail = "The request contains an invalid value.";
        }

        ProblemDetailDTO problemDetailDto = createProblemDetailDto(
                httpStatus,
                problemDetailType,
                detail,
                null
        );
        return handleExceptionInternal(ex, problemDetailDto, new HttpHeaders(), httpStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        log.warn(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ProblemDetailType problemDetailType = ProblemDetailType.INVALID_DATA;
        ProblemDetailDTO problemDetailDto = createProblemDetailDto(
                httpStatus,
                problemDetailType,
                problemDetailType.getMessage(),
                toFields(ex.getBindingResult().getAllErrors())
        );
        return handleExceptionInternal(ex, problemDetailDto, new HttpHeaders(), httpStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        log.warn(ex.getMessage(), ex);
        if (ex instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
            ProblemDetailType problemDetailType = ProblemDetailType.INVALID_PARAMETER;
            String detail = String.format("The parameter '%s' received the value '%s', which is of an invalid type. Correct and provide a value compatible with the type %s.",
                    methodArgumentTypeMismatchException.getPropertyName(),
                    methodArgumentTypeMismatchException.getValue(),
                    methodArgumentTypeMismatchException.getParameter().getParameterType().getSimpleName()
            );
            ProblemDetailDTO problemDetailDto = createProblemDetailDto(
                    httpStatus,
                    problemDetailType,
                    detail,
                    null
            );
            return handleExceptionInternal(ex, problemDetailDto, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        log.warn(ex.getMessage(), ex);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ProblemDetailType problemDetailType = ProblemDetailType.RESOURCE_NOT_FOUND;
        String detail = String.format("The resource '%s' who you tried to access is not found.", ex.getResourcePath());
        ProblemDetailDTO problemDetailDto = createProblemDetailDto(
                httpStatus,
                problemDetailType,
                detail,
                null
        );
        return handleExceptionInternal(ex, problemDetailDto, new HttpHeaders(), httpStatus, request);
    }

    private ProblemDetailDTO createProblemDetailDto(HttpStatus httpStatus, ProblemDetailType problemDetailType,
                                                                                        String detail,  List<ProblemDetailFieldDTO> fields) {
        return problemDetailMapper.toProblemDetailDto(
                httpStatus.value(),
                problemDetailType.getTitle(),
                detail,
                problemDetailType.getMessage(),
                NOW,
                fields
        );
    }

    private List<ProblemDetailFieldDTO> toFields(List<ObjectError> objectErrors) {
        if (CollectionUtils.isEmpty(objectErrors)) {
            return Collections.emptyList();
        }

        return objectErrors.stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError fieldError) {
                        name = fieldError.getField();
                    }

                    return problemDetailMapper.toProblemDetailFieldDto(name, message);
                })
                .toList();
    }
}
