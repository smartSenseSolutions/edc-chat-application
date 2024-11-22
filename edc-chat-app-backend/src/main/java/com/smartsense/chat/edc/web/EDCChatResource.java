package com.smartsense.chat.edc.web;

import com.smartsense.chat.edc.EDCService;
import com.smartsense.chat.utils.request.ChatMessage;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs.EDCChatReceive;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs.EDCChatSent;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "EDC Chat management Controller", description = "This controller used to manage chat.")
@Slf4j
public class EDCChatResource {
    private final EDCService edcService;
    private final SimpMessagingTemplate messagingTemplate;

    @EDCChatSent
    @PostMapping("/chat")
    public Map<String, String> sentMessage(@RequestBody ChatMessage chatMessage) {
        edcService.initProcess(chatMessage);
        return Map.of("message", "Send message process has been started, please check the logs for more details.");
    }

    @EDCChatReceive
    @PostMapping("/chat/receive")
    public Map<String, String> receiveMessage(@RequestBody ChatMessage message) {
        // add
        log.info("Message: {}", message.message());
        //messagingTemplate.convertAndSendToUser(message.senderBpn(), "/queue/messages", message.message());
        return Map.of("message", "Message had been received successfully.");
    }
}
