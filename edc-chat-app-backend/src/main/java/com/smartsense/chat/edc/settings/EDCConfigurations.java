package com.smartsense.chat.edc.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("chat.edc")
public record EDCConfigurations(String authCode,
                                String assetId) {
}
