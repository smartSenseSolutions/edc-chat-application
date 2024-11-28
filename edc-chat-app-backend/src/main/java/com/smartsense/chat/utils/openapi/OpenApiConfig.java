/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.utils.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info();
        info.setTitle("EDC based chat application");
        info.setDescription("EDC based chat application");
        OpenAPI openAPI = new OpenAPI();
        return openAPI.info(info);
    }

    @Bean
    public GroupedOpenApi openApiDefinition() {
        return GroupedOpenApi.builder()
                .group("docs")
                .pathsToMatch("/**")
                .displayName("Docs")
                .build();
    }

}
