package com.smartsense.chat.edc.settings;

import java.net.URI;

public record EDCConfigurations(String edcUrl,
                                String authCode,
                                String assetId,
                                String contractDefinitionId,
                                String policyId,
                                String agreementWaitTime,
                                int agreementRetryLimit) {

    public URI edcUri() {
        return URI.create(edcUrl);
    }
}
