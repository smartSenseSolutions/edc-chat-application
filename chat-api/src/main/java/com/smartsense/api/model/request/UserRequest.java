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

import com.smartsense.api.model.BaseModel;
import jakarta.validation.constraints.NotEmpty;


/**
 * Create a new {@code UserRequest} with the given first name, last name, and email.
 *
 * @param firstName the user's first name (must not be empty)
 * @param lastName  the user's last name (must not be empty)
 * @param email     the user's email (must not be empty)
 */
public record UserRequest(@NotEmpty(message = "{please.enter.user.first.name}") String firstName,
                          String lastName,
                          @NotEmpty(message = "{please.enter.email}") String email,
                          String bpn) implements BaseModel {
}
