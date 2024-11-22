package com.smartsense.chat.edc.web;

import com.smartsense.chat.edc.EDCService;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.utils.request.ChatRequest;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs.EDCChatReceive;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Tag(name = "EDC Chat management Controller", description = "This controller used to manage chat.")
public class EDCChatResource {
    private final EDCService edcService;
    private final AppConfig config;


    @EDCChatApiDocs.GetChatHistory
    @GetMapping(value = "chat/history", produces = APPLICATION_JSON_VALUE)
    public List<Map> getChatHistory(@RequestParam(name = "partnerBpn") String partnerBpn) {
        return edcService.getChatHistory(partnerBpn);
    }

    @EDCChatApiDocs.Chat
    @PostMapping("/chat")
    public Map<String, String> sentMessage(@RequestBody ChatRequest chatRequest) {
        edcService.initProcess(chatRequest);
        return Map.of("message", "Send message process has been started, please check the logs for more details.");
    }

    @EDCChatReceive
    @PostMapping("/chat/receive")
    public Map<String, String> receiveMessage(@RequestBody ChatRequest message) {
        edcService.receiveMessage(message);
        return Map.of("message", "Message had been received successfully.");
    }
}
