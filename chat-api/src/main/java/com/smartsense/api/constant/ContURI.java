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
package com.smartsense.api.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * All URL and partial URL constant
 *
 * @author Sunil Kanzar
 * @since 20th of Dec 2023
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContURI {
    //User Management
    public static final String USER = "/common/user";
    public static final String USER_WITH_ID = "/common/user/{userId}";
    public static final String USER_FILTER = "/common/user/filter";

    // EDC Register
    public static final String EDC_CREATE = "common/edc/create";
    public static final String EDC_UPDATE = "common/edc/update";
    public static final String EDC_FILTER = "common/edc/filter";
    public static final String GET_EDC = "common/edc/{edcId}";
    public static final String GET_EDC_USER = "/common/user/edc/{userId}";

    // Chat
    public static final String CHAT_REGISTER = "common/chat";

    //Context
    public static final String CONTEXT_AUTHENTICATED_ALL = "/common/**";
    public static final String CONTEXT_AUTHENTICATED_WITH_NO_FILTER_ALL = "/common2/**";
    public static final String CONTEXT_PRIVATE_ALL = "/private/**";
    public static final String CONTEXT_HEALTH_ALL = "/actuator/health/**";
    public static final String CONTEXT_SWAGGER_IU_ALL = "/ui/swagger-ui/**";
    public static final String CONTEXT_API_DOCS_ALL = "/docs/api-docs/**";
    public static final String CONTEXT_ALL = "/**";
}
