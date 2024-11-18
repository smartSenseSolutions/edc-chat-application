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
package com.smartsense.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code MultipartFileSize} annotation is for validation of size of the multipart file.
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@Documented
@Constraint(validatedBy = { MultipartFileSizeValidator.class })
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartFileSize {

    /**
     * Validation Message
     *
     * @return validation message
     */
    String message() default "The size of the files must be between {min} and {max} bytes.";

    /**
     * Minimum size of uploaded file.
     *
     * @return minimum size
     */
    long min() default 0;

    /**
     * Maximum size of uploaded file
     *
     * @return max size
     */
    long max() default Long.MAX_VALUE;

    /**
     * @return class
     */
    Class<?>[] groups() default {};

    /**
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};
}
