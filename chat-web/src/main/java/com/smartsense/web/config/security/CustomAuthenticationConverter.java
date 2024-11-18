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

import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import com.smartsense.api.constant.ContField;
import com.smartsense.api.constant.ContValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * CustomAuthenticationConverter is used to extract the role information from the JWT token.
 * This will use when security configuration is enabled in the application.
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
public class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter;
    private final String resourceId;

    CustomAuthenticationConverter(String resourceId) {
        this.resourceId = resourceId;
        grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    /**
     * Method used for extracting the scope of the jwt token and manage the authority details.
     *
     * @param source - Indicates the Jwt
     * @return {@link AbstractAuthenticationToken}
     */
    @Override
    public AbstractAuthenticationToken convert(@NotNull Jwt source) {
        Collection<GrantedAuthority> authorities = new HashSet<>((grantedAuthoritiesConverter.convert(source)));
        authorities.addAll(extractResourceRoles(source, resourceId));
        return new JwtAuthenticationToken(source, authorities);
    }

    /**
     * Extract the roles from resource_access from the JWT token.
     *
     * @param jwt        - Indicates the JWT token
     * @param resourceId - Indicates the clientId which used by the resource server.
     * @return Collection Of {@link GrantedAuthority}
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt, String resourceId) {
        Map<String, Object> resourceAccess = jwt.getClaim(ContField.CLAIM_RESOURCE_ACCESS);
        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(resourceId);
        if (Objects.isNull(resource)) {
            return Set.of();
        }
        Collection<String> resourceRoles = (Collection<String>) resource.get(ContField.CLAIM_ROLES);
        if (Objects.isNull(resourceRoles)) {
            return Set.of();
        }
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority(ContValue.SPRING_ROLE_PREFIX + role))
                .collect(Collectors.toSet());
    }
}
