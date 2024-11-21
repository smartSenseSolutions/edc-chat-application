package com.smartsense.chat.utils.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

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
