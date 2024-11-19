package com.smartsense.api.model.response;

import java.util.UUID;

public record EDCRegisterResponse(
        UUID id,
        String edcUrl,
        UserResponse userId
) {
}
