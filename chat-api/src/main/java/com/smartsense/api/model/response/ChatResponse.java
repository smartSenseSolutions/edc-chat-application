package com.smartsense.api.model.response;

import java.util.UUID;

public record ChatResponse(UUID id,
                           UUID senderId,
                           UUID receiverId,
                           String message,
                           String messageType,
                           String createdAt) {
}
