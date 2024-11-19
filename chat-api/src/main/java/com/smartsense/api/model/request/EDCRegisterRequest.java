package com.smartsense.api.model.request;

import java.util.UUID;

public record EDCRegisterRequest(String edcUrl, UUID userId) {
}
