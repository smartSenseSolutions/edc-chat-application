package com.smartsense.api.model.response;

import java.util.List;
import java.util.UUID;

public record UsersEDCResponse(UUID userId,
                               List<String> edcUrl) {
}
