package com.smartsense.web.resource;

import com.smartsense.api.constant.ContMessage;
import com.smartsense.api.constant.ContURI;
import com.smartsense.api.model.request.ChatRequest;
import com.smartsense.api.model.response.ChatResponse;
import com.smartsense.api.model.response.ResponseBody;
import com.smartsense.service.ChatService;
import com.smartsense.web.apidocs.ChatResourceApiDocs.ChatCreated;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatResource extends BaseResource {

    private final ChatService chatService;

    @ChatCreated
    @Operation(summary = "Chat")
    @PostMapping(value = ContURI.CHAT_REGISTER, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatResponse chatResponse = chatService.createChat(request);
        return ResponseBody.of(resolveMessage(ContMessage.CHAT_CREATED), chatResponse);
    }
}
