package com.smartsense.chat.utils.exception;

import com.smartsense.chat.utils.constant.ContField;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalExceptionHandler {

    /**
     * Handle validation problem detail.
     *
     * @param e the e
     * @return the problem detail
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(e));
        problemDetail.setTitle("Invalid data provided");
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        problemDetail.setProperty(ContField.ERRORS, handleValidationError(e.getFieldErrors()));
        return problemDetail;
    }

    /**
     * Handle validation problem detail.
     *
     * @param exception the exception
     * @return the problem detail
     */
    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleValidation(ConstraintViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(exception));
        problemDetail.setTitle("Invalid data provided");
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        problemDetail.setProperty(ContField.ERRORS, exception.getConstraintName());
        return problemDetail;
    }

    /**
     * Handle bad data exception problem detail.
     *
     * @param e the e
     * @return the problem detail
     */
    @ExceptionHandler(com.smartsense.chat.utils.exception.BadDataException.class)
    ProblemDetail handleBadDataException(com.smartsense.chat.utils.exception.BadDataException e) {
        String errorMsg = ExceptionUtils.getMessage(e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMsg);
        problemDetail.setTitle(errorMsg);
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        return problemDetail;
    }

    /**
     * Handle property reference exception problem detail.
     *
     * @param exception the exception
     * @return the problem detail
     */
    @ExceptionHandler(PropertyReferenceException.class)
    ProblemDetail handlePropertyReferenceException(PropertyReferenceException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(exception));
        problemDetail.setTitle(ExceptionUtils.getMessage(exception));
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        problemDetail.setProperty(ContField.PROPERTY, exception.getPropertyName());
        return problemDetail;
    }

    /**
     * Handle method argument type mismatch exception problem detail.
     *
     * @param exception the exception
     * @return the problem detail
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(exception));
        problemDetail.setTitle(ExceptionUtils.getMessage(exception));
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        problemDetail.setProperty(ContField.ARGUMENT, exception.getName());
        return problemDetail;
    }

    /**
     * Handle illegal argument exception problem detail.
     *
     * @param exception the exception
     * @return the problem detail
     */
    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgumentException(IllegalArgumentException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(exception));
        problemDetail.setTitle(ExceptionUtils.getMessage(exception));
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        return problemDetail;
    }

    /**
     * Handle exception problem detail.
     *
     * @param e the e
     * @return the problem detail
     */
    @ExceptionHandler(Exception.class)
    ProblemDetail handleException(Exception e) {
        log.error("Error ", e);
        ProblemDetail problemDetail;
        problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getMessage(e));
        problemDetail.setTitle(ExceptionUtils.getMessage(e));
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        return problemDetail;
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflictException(ConflictException ex) {
        String errorMsg = ExceptionUtils.getMessage(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, errorMsg);
        problemDetail.setTitle(errorMsg);
        problemDetail.setProperty(ContField.TIMESTAMP, System.currentTimeMillis());
        return problemDetail;
    }

    private Map<String, String> handleValidationError(List<FieldError> fieldErrors) {
        Map<String, String> messages = new HashMap<>();
        fieldErrors.forEach(fieldError -> messages.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return messages;
    }
}

