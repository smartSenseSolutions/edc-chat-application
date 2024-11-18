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

package com.smartsense.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakTestConstant {
    public static final String BEARER = "Bearer ";
    public static final String REAL_FILE_PATH = "app-test-realm.json";
    public static final String KEYCLOAK_ADMIN_USERNAME = "admin";
    public static final String KEYCLOAK_REALM = "SWD";
    public static final String KEYCLOAK_PUBLIC_CLIENT = "pb_backend";
    public static final String KEYCLOAK_PRIVATE_CLIENT = "pv_backend";
    public static final String VALID_USER_NAME = "valid-user";
    public static final String INVALID_USER_NAME = "invalid-user";
    public static final String PASSWORD = "password"; //pragma: allowlist secret

}
