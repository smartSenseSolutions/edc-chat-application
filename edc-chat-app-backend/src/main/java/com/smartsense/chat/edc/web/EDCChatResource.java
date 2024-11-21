package com.smartsense.chat.edc.web;

import com.smartsense.chat.edc.EDCService;
import com.smartsense.chat.utils.request.ChatMessage;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs.EDCChatReceive;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs.EDCChatSent;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "EDC Chat management Controller", description = "This controller used to manage chat.")
public class EDCChatResource {
    private final EDCService edcService;

    @EDCChatSent
    @PostMapping("/chat")
    public Map<String, String> sentMessage(@RequestBody ChatMessage chatMessage) {
        edcService.initProcess(chatMessage);
        return Map.of("message", "Send message process has been started, please check the logs for more details.");
    }

    @EDCChatReceive
    @PostMapping("/chat/receive")
    public Map<String, String> receiveMessage(@RequestBody ChatMessage message) {
        return Map.of("message", "Message had been received successfully.");
    }
}
