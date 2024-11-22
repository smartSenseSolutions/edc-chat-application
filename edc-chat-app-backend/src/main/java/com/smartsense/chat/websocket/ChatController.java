package com.smartsense.chat.websocket;

import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {

    @MessageMapping("/message") //received by client
    @SendTo("/topic/message") //sent to client
    @SneakyThrows
    public ChatMessage greeting(@Payload ChatMessage userMessage) {
        return new ChatMessage("Hello, " + HtmlUtils.htmlEscape(userMessage.message()) + "!");
    }
}
