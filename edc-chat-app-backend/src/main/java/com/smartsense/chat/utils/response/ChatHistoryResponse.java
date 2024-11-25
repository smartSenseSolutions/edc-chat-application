package com.smartsense.chat.utils.response;


public record ChatHistoryResponse(Long id,
                                  String receiver,
                                  String sender,
                                  String content,
                                  String status,
                                  long timestamp) {
}
