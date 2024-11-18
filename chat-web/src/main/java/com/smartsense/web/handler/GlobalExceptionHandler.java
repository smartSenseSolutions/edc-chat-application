/*
 * Copyright 2024 smartSense Consulting Solutions Pvt. Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartsense.web.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.smartsense.api.constant.ContField;
import com.smartsense.api.exception.BadDataException;
import com.smartsense.service.entity.BaseService;

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
public class GlobalExceptionHandler extends BaseService {

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
        problemDetail.setProperty(ContField.ERRORS, exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList());
        return problemDetail;
    }

    /**
     * Handle bad data exception problem detail.
     *
     * @param e the e
     * @return the problem detail
     */
    @ExceptionHandler(BadDataException.class)
    ProblemDetail handleBadDataException(BadDataException e) {
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

    private Map<String, String> handleValidationError(List<FieldError> fieldErrors) {
        Map<String, String> messages = new HashMap<>();
        fieldErrors.forEach(fieldError -> messages.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return messages;
    }
}
