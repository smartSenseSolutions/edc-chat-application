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

package com.smartsense.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class TestHelper {

    private static Environment env;

    @Autowired
    public TestHelper(Environment environment) {
        TestHelper.env = environment;
    }

    public static MultiValueMap<String, String> prepareHeader(boolean validUser) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        if (Boolean.parseBoolean(TestHelper.env.getProperty("app.security.enabled"))) {
            headers.add(HttpHeaders.AUTHORIZATION, ContainerContextInitializer.createToken(validUser));
        }
        return headers;
    }
}
