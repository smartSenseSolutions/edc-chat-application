package com.smartsense.chat.edc.web;

import com.smartsense.chat.utils.request.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/privateMessage")
    public ChatMessage privateMessage(@RequestBody ChatMessage message) {
        messagingTemplate.convertAndSendToUser(message.receiverBpn(), "/queue/messages", message);
        return message;
    }

}
