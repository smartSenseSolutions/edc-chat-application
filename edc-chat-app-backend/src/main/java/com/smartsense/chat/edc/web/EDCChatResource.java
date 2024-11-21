package com.smartsense.chat.edc.web;

import com.smartsense.chat.edc.EDCService;
import com.smartsense.chat.utils.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EDCChatResource {
    private final EDCService edcService;

    @PostMapping("/chat")
    public Map<String, String> sentMessage(@RequestBody ChatMessage chatMessage) {
        edcService.initProcess(chatMessage);
        return Map.of("message", "Send message process has been started, please check the logs for more details.");
    }

    @PostMapping("/chat/receive")
    public Map<String, String> receiveMessage(@RequestBody ChatMessage message) {
        
        return Map.of("message", "Message had been received successfully.");
    }
}
