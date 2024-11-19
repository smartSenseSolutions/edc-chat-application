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
 * Validation and response message related constant
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContMessage {

    public static final String SOMETHING_WENT_WRONG = "something.went.wrong";
    public static final String USER_CREATED = "user.created.successfully";
    public static final String EDC_CREATED = "edc.created.successfully";
    public static final String CHAT_CREATED = "chat.created.successfully";
    public static final String VALIDATE_USER_CREATE_NAME_EXIST = "validate.user.create.name.exist";
    public static final String VALIDATE_EDC_CREATE_URL_EXIST = "validate.edc.create.url.exist";
}
