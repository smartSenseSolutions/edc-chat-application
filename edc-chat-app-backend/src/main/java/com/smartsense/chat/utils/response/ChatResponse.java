package com.smartsense.chat.utils.response;

public record ChatResponse(String receiver, String sender, String content, long timestamp) {
}
