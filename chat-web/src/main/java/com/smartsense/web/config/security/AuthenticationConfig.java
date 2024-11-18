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
package com.smartsense.web.config.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.smartsense.api.constant.ContURI;
import com.smartsense.api.constant.ContValue;

import java.util.List;

/**
 * AuthenticationConfig provided the security to the endpoint.
 * Each and every call first authenticate from here and after that will go to the controller level.
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@Slf4j
@EnableWebSecurity
@Configuration
@AllArgsConstructor
@EnableMethodSecurity()
public class AuthenticationConfig {

    private final SecurityConfigProperties configProperties;

    /**
     * This is a conditional bean which will be created if app.security.enabled= true.
     * This contains the basic role authentication mechanism for the endpoint that we introduced at controller layer.
     *
     * @param http - Indicate HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception - Indicates the exception
     */
    @Bean
    @ConditionalOnProperty(value = "app.security.enabled", havingValue = "true", matchIfMissing = true)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(new AntPathRequestMatcher(ContURI.CONTEXT_ALL, HttpMethod.OPTIONS.name())).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher(ContURI.CONTEXT_API_DOCS_ALL)).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher(ContURI.CONTEXT_SWAGGER_IU_ALL)).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher(ContURI.CONTEXT_HEALTH_ALL)).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher(ContURI.CONTEXT_PRIVATE_ALL)).permitAll();
                    auth.requestMatchers(new AntPathRequestMatcher(ContURI.CONTEXT_AUTHENTICATED_ALL)).hasRole("SWD_ADMIN");
                    auth.requestMatchers(new AntPathRequestMatcher(ContURI.CONTEXT_AUTHENTICATED_WITH_NO_FILTER_ALL)).hasRole("SWD_ADMIN");
                }).oauth2ResourceServer(oath -> oath.jwt(jwt -> jwt.jwtAuthenticationConverter(new CustomAuthenticationConverter(configProperties.roleClientId()))));
        return http.build();
    }


    /**
     * This is a conditional bean which will be created if app.security.enabled= false.
     * This is not recommended configuration that will use in production environments.
     *
     * @return {@link WebSecurityCustomizer} - Indicates web security customizer
     */
    @Bean
    @ConditionalOnProperty(value = "app.security.enabled", havingValue = "false")
    public static WebSecurityCustomizer securityCustomizer() {
        AuthenticationConfig.log.warn("Disable security : This is not recommended to use in production environments.");
        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("**"));
    }

    /**
     * This is a Bean which will be manage the cors configuration.
     *
     * @return {@link CorsConfigurationSource}
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(configProperties.corsOrigins());
        configuration.setAllowedMethods(List.of(HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(), HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setAllowedHeaders(List.of(ContValue.ALLOWED_HEADERS));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ContURI.CONTEXT_ALL, configuration);
        return source;
    }

    /**
     * This bean is used to publish authentication events, such as successful or failed login attempts.
     *
     * @param applicationEventPublisher - This instance is used to publish events to the application context.
     * @return An instance of DefaultAuthenticationEventPublisher, which implements the AuthenticationEventPublisher interface.
     * @see AuthenticationEventPublisher
     * @see DefaultAuthenticationEventPublisher
     * @see ApplicationEventPublisher
     */
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}

