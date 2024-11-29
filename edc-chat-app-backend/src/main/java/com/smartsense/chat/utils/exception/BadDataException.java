/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.utils.exception;

import java.io.Serial;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BadDataException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5732404099105408974L;

    private String code;
    private String message;

    public BadDataException(String message) {
        this.message = message;
    }

    public BadDataException(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
