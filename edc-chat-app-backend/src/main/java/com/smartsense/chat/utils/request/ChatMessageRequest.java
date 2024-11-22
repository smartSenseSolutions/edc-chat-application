package com.smartsense.chat.utils.request;

public record ChatMessageRequest(String senderBpn,
                                 String receiverBpn,
                                 String message) {
}
