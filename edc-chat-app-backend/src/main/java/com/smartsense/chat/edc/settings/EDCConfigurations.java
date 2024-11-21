package com.smartsense.chat.edc.settings;

import java.net.URI;

public record EDCConfigurations(String edcUrl,
                                String authCode,
                                String assetId) {

    public URI edcUri() {
        return URI.create(edcUrl);
    }
}
