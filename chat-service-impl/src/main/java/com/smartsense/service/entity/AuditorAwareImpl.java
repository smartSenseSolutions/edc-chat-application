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
package com.smartsense.service.entity;

import org.springframework.data.domain.AuditorAware;
import com.smartsense.api.constant.ContValue;

import java.util.Optional;

/**
 * Provide audit related information for auditable entity
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Fetch UserId from SecurityContext and return Optional Value
     *
     * @return Optional of {@link String}
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        String authUserId = "";
        return !authUserId.isEmpty() ? Optional.of(authUserId) : Optional.of(ContValue.ANONYMOUS);
    }
}
