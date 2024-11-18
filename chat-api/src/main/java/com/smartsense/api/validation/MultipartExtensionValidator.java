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

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@code MultipartExtensionValidator} is validator class for validating extension of uploaded file/multipart file.
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
public class MultipartExtensionValidator implements ConstraintValidator<MultipartFileExtension, MultipartFile> {

    /**
     * String array of allowed extensions
     */
    private String[] extensions;

    @Override
    public void initialize(MultipartFileExtension constraintAnnotation) {
        extensions = constraintAnnotation.extension();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        boolean isValid = false;
        if (value != null) {
            for (String extension : extensions) {
                if (value.getOriginalFilename() != null && value.getOriginalFilename().toLowerCase().endsWith(extension.toLowerCase())) {
                    return true;
                }
            }
        } else {
            isValid = true;
        }
        return isValid;
    }
}
