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
package com.smartsense.web.config;

import lombok.AllArgsConstructor;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.smartsense.api.constant.ContURI;
import com.smartsense.api.constant.ContValue;

/**
 * Web MVC Config
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@AllArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final SwaggerUiConfigProperties properties;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String redirectUri = properties.getPath();
        registry.addRedirectViewController(ContValue.FORWARD_SLASH, redirectUri);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns(ContURI.CONTEXT_AUTHENTICATED_ALL);
    }
}
