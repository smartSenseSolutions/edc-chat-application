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

package com.smartsense.api.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Common response format.
 * PageResponse Used for any paginated response.
 *
 * @param <T> - Indicates any type of response
 * @author Sunil Kanzar
 * @since 29th of Dec 2023
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseBody<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7137695452659404087L;

    private String message;
    private T body;

    public static <T> ResponseBody<T> of(String message, T body) {
        return new ResponseBody<>(message, body);
    }
}
