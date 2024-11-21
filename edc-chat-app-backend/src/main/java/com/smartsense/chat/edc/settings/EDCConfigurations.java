package com.smartsense.chat.edc.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("chat.edc")
public record EDCConfigurations(String edcUrl,
                                String authCode,
                                String assetId) {

    public URI edcUri() {
        return URI.create(edcUrl);
    }
}
