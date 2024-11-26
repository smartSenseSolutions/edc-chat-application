package com.smartsense.chat.edc.web;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.edc.service.EDCService;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.utils.request.ChatRequest;
import com.smartsense.chat.utils.response.ChatHistoryResponse;
import com.smartsense.chat.utils.response.MessageStatus;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs.EDCChatReceive;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.smartsense.chat.web.ApiConstant.CHAT_HISTORY;
import static com.smartsense.chat.web.ApiConstant.RECEIVE_CHAT;
import static com.smartsense.chat.web.ApiConstant.SEND_CHAT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Tag(name = "EDC Chat management Controller", description = "This controller used to manage chat.")
public class EDCChatResource {
    private static final Logger log = LoggerFactory.getLogger(EDCChatResource.class);
    private final EDCService edcService;
    private final AppConfig config;
    private final SimpMessagingTemplate messagingTemplate;
    private final AppConfig appConfig;


    @EDCChatApiDocs.GetChatHistory
    @GetMapping(value = CHAT_HISTORY, produces = APPLICATION_JSON_VALUE)
    public List<ChatHistoryResponse> getChatHistory(@RequestParam(name = "partnerBpn") String partnerBpn) {
        return edcService.getChatHistory(partnerBpn);
    }

    @EDCChatApiDocs.Chat
    @PostMapping(SEND_CHAT)
    public Map<String, String> sentMessage(@RequestBody ChatRequest chatRequest) {
        edcService.initProcess(chatRequest);
        return Map.of("message", "Send message process has been started, please check the logs for more details.");
    }

    @EDCChatReceive
    @PostMapping(RECEIVE_CHAT)
    public Map<String, String> receiveMessage(@RequestBody ChatRequest message) {
        ChatMessage chatMessage = edcService.receiveMessage(message);
        ChatHistoryResponse chatResponse = new ChatHistoryResponse(chatMessage.getId(), message.receiverBpn(), appConfig.bpn(),
                message.message(), MessageStatus.SENT, chatMessage.getCreatedAt().getTime(), null);
        messagingTemplate.convertAndSend("/topic/messages", chatResponse);
        return Map.of("message", "Message had been received successfully.");
    }
}
