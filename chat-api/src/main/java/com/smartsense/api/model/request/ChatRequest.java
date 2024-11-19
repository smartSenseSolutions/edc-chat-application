package com.smartsense.api.model.request;

import java.util.UUID;

public record ChatRequest(UUID senderId,
                          UUID receiverId,
                          String message,
                          String messageType) {
}
