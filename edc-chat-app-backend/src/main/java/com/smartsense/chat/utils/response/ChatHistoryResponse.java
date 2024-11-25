package com.smartsense.chat.utils.response;


public record ChatHistoryResponse(Long id,
                                  String receiver,
                                  String sender,
                                  String content,
                                  MessageStatus status,
                                  long timestamp, String errorMessage) {
}
