package com.smartsense.api.model.request;

import java.util.UUID;

public record EDCUpdateRequest(UUID edcId, EDCRegisterRequest request) {
}
