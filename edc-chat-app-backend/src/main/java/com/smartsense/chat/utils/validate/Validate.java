/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.utils.validate;


import com.smartsense.chat.utils.exception.BadDataException;

import java.util.Objects;
import java.util.function.Function;


public class Validate<T> {
    private T value;
    private boolean match = false;

    private Validate() {
    }

    private Validate(T value) {
        this.value = value;
    }

    public static <V> Validate<V> value(V value) {
        return new Validate<>(value);
    }

    public static <V> Validate<V> isTrue(boolean condition) {
        Validate<V> validate = new Validate<>();
        if (condition) {
            validate.match = true;
        }
        return validate;
    }

    /**
     * Throws if {@code condition} is false
     *
     * @param condition the condition
     * @param <V>
     * @return
     */
    public static <V> Validate<V> isFalse(boolean condition) {
        Validate<V> validate = new Validate<>();
        if (!condition) {
            validate.match = true;
        }
        return validate;
    }

    public static <T> Validate<T> isNull(T value) {
        return new Validate<>(value).isNull();
    }

    public static <T> Validate<T> isNotNull(T value) {
        return new Validate<>(value).isNotNull();
    }

    public Validate<T> inLength(int min, int max) {
        if (Objects.isNull(value)) {
            return this;
        }
        if (match || value.toString().length() < min && value.toString().length() > max) {
            match = true;
        }
        return this;
    }

    public Validate<T> isNotEmpty() {
        if (match || Objects.isNull(value) || String.valueOf(value).trim().isEmpty()) {
            match = true;
        }
        return this;
    }

    public Validate<T> isNull() {
        if (match || Objects.isNull(value)) {
            match = true;
        }
        return this;
    }

    public Validate<T> isNotNull() {
        if (match || !Objects.isNull(value)) {
            match = true;
        }
        return this;
    }

    public Validate<T> check(Function<T, Boolean> checkFunction) {
        if (match || checkFunction.apply(value)) {
            match = true;
        }
        return this;
    }

    public Validate<T> checkNot(Function<T, Boolean> checkFunction) {
        if (match || !checkFunction.apply(value)) {
            match = true;
        }
        return this;
    }

    /**
     * Throw passed exception if expression is match
     *
     * @param e exception to throw
     */
    public T launch(RuntimeException e) {
        if (match) {
            throw e;
        }
        return value;
    }

    /**
     * Throw {@code BadDataException} if expression is match with passed message
     *
     * @param message exception message
     */
    public T launch(String message) {
        if (match) {
            throw new BadDataException(message);
        }
        return value;
    }

    /**
     * Calculate all of the conditions are true or not
     *
     * @return true if any of condition are true
     */
    public boolean calculate() {
        return match;
    }
}
