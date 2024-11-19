package com.smartsense.service;

import com.smartsense.api.model.request.ChatRequest;
import com.smartsense.api.model.response.ChatResponse;

public interface ChatService {

    ChatResponse createChat(ChatRequest request);
}
