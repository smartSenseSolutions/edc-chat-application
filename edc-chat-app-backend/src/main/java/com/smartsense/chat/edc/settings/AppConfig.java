package com.smartsense.chat.edc.settings;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties("chat")
public record AppConfig(String bpn,
                        String assetUrl,
                        EDCConfigurations edc) {

    @PostConstruct
    public void checkingConfig() {
        if (!StringUtils.hasText(bpn)) {
            throw new RuntimeException("Please provide bpn with chat.bpn configurations.");
        }
        if (!StringUtils.hasText(assetUrl)) {
            throw new RuntimeException("Please provide bpn with chat.assetUrl configurations.");
        }
        if (!StringUtils.hasText(edc().assetId())) {
            throw new RuntimeException("Please provide bpn with chat.edc.assetId configurations.");
        }
        if (!StringUtils.hasText(edc().edcUrl())) {
            throw new RuntimeException("Please provide bpn with chat.edc.edcUrl configurations.");
        }
        if (!StringUtils.hasText(edc().authCode())) {
            throw new RuntimeException("Please provide bpn with chat.edc.authCode configurations.");
        }
    }
}
