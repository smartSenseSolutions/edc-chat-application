package com.smartsense.chat.utils.request;

public record ChatMessage(String senderBpn,
                          String receiverBpn,
                          String message,
                          String messageId) {
}
