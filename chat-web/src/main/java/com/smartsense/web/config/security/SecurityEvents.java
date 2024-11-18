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

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class is responsible for handling security events such as failed authentication and authorization.
 * It uses Spring's EventListener annotation to listen for specific events and logs relevant information.
 */
@Component
@Slf4j
public class SecurityEvents {

    /**
     * Event listener method to handle failed authentication events.
     * This method logs a message when a failed authentication occurs due to an invalid 'Bearer' token.
     *
     * @param failures It's containing details about the failed authentication.
     */
    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        String excMessage = failures.getException().getMessage();
        log.warn("Failed Authentication --> Invalid 'Bearer' token. {}", excMessage);
    }

    /**
     * Event listener method to handle failed authorization events.
     * This method logs a message when a failed authorization occurs due to a missing 'Authorization' header.
     *
     * @param failure It's containing details about the failed authorization.
     */
    @EventListener
    public void onFailure(AuthorizationDeniedEvent<Object> failure) {
        AuthorizationDecision decision = failure.getAuthorizationDecision();
        if (decision instanceof AuthorityAuthorizationDecision authorityAuthorizationDecision) {
            List<GrantedAuthority> authorities = authorityAuthorizationDecision.getAuthorities().stream().toList();
            log.warn("Failed Authorization --> Missing 'Authorization' header OR Required roles are missing in the token. Required roles are {}", authorities);
        } else {
            log.warn("Failed Authorization --> Some issue in authorization.");
        }
    }
}
