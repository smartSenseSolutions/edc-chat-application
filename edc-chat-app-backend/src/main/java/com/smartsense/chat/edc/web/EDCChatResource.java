package com.smartsense.chat.edc.web;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.edc.EDCService;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.utils.request.ChatRequest;
import com.smartsense.chat.utils.response.ChatHistoryResponse;
import com.smartsense.chat.utils.response.MessageStatus;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs;
import com.smartsense.chat.web.apidocs.EDCChatApiDocs.EDCChatReceive;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

import static com.smartsense.chat.web.ApiConstant.CHAT_HISTORY;
import static com.smartsense.chat.web.ApiConstant.RECEIVE_CHAT;
import static com.smartsense.chat.web.ApiConstant.SEND_CHAT;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "EDC Chat management Controller", description = "This controller used to manage chat.")
public class EDCChatResource {
    private static final Logger log = LoggerFactory.getLogger(EDCChatResource.class);
    private final EDCService edcService;
    private final AppConfig config;
    private final SimpMessagingTemplate messagingTemplate;
    private final AppConfig appConfig;

    /**
     * Retrieves the chat history for a specific partner.
     * <p>
     * This method handles GET requests to fetch the chat history associated with a given
     * partner BPN (Business Partner Number). It returns a list of chat messages in
     * chronological order.
     *
     * @param partnerBpn The Business Partner Number (BPN) of the partner for which
     *                   the chat history is being requested.
     * @return A List of ChatHistoryResponse objects representing the chat history
     * with the specified partner. Each object in the list contains details
     * of a single chat message.
     */
    @EDCChatApiDocs.GetChatHistory
    @GetMapping(value = CHAT_HISTORY, produces = APPLICATION_JSON_VALUE)
    public List<ChatHistoryResponse> getChatHistory(@RequestParam(name = "partnerBpn") String partnerBpn) {
        return edcService.getChatHistory(partnerBpn);
    }


    /**
     * Initiates the process of sending a chat message.
     * <p>
     * This method handles the sending of a chat message by initiating the process
     * through the EDCService. It returns a confirmation message to the client.
     *
     * @param chatRequest The ChatRequest object containing the details of the message to be sent.
     * @return A Map containing a single key-value pair. The key is "message" and the value
     * is a string indicating that the send message process has been started.
     */
    @EDCChatApiDocs.Chat
    @PostMapping(SEND_CHAT)
    public Map<String, String> sentMessage(@RequestBody ChatRequest chatRequest) {
        edcService.initProcess(chatRequest);
        return Map.of("message", "Send message process has been started, please check the logs for more details.");
    }


    /**
     * Receives and processes an incoming chat message from sender EDC.
     * <p>
     * This method handles the reception of a chat message, saves it to the database,
     * creates a response, and broadcasts the message to subscribed clients via WebSocket.
     *
     * @param message The ChatRequest object containing the incoming message details.
     * @return A Map containing a single key-value pair, where the key is "message" and
     * the value is a string confirming successful reception of the message.
     */
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
