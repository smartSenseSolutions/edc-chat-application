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
package com.smartsense.api.utils;


import com.smartsense.api.exception.BadDataException;

import java.util.Objects;
import java.util.function.Function;

/**
 * Class to simplify validation<br>
 * Build validation sequence and can be thrown or calculate
 *
 * @param <T> Type of value
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
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
