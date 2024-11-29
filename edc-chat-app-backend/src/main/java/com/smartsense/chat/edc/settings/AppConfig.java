/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.settings;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties("chat")
public record AppConfig(String bpn,
                        EDCConfigurations edc) {

    @PostConstruct
    public void checkingConfig() {
        if (!StringUtils.hasText(bpn)) {
            throw new IllegalStateException("Please provide bpn with chat.bpn configurations.");
        }
        if (!StringUtils.hasText(edc().policyId())) {
            throw new IllegalStateException("Please provide bpn with chat.edc.assetId configurations.");
        }
        if (!StringUtils.hasText(edc().assetId())) {
            throw new IllegalStateException("Please provide bpn with chat.edc.policyId configurations.");
        }
        if (!StringUtils.hasText(edc().contractDefinitionId())) {
            throw new IllegalStateException("Please provide bpn with chat.edc.contractDefinitionId configurations.");
        }
        if (!StringUtils.hasText(edc().edcUrl())) {
            throw new IllegalStateException("Please provide bpn with chat.edc.edcUrl configurations.");
        }
        if (!StringUtils.hasText(edc().authCode())) {
            throw new IllegalStateException("Please provide bpn with chat.edc.authCode configurations.");
        }
    }
}
