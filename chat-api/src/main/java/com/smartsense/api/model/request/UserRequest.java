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
package com.smartsense.api.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import com.smartsense.api.model.BaseModel;

/**
 * User request
 *
 * @param name    name of user
 * @param age     age of user
 * @param city    city of user
 * @param country country of city
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
public record UserRequest(@NotEmpty(message = "{please.enter.user.name}") String name,
                          @Min(value = 18, message = "{you.are.under.age}") Integer age,
                          @NotEmpty(message = "{please.enter.city}") String city,
                          @NotEmpty(message = "{please.enter.county}") String country) implements BaseModel {
}
