package com.smartsense.chat.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WebSocketSessionChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MessageHeaders headers = message.getHeaders();

        MultiValueMap<String, String> multiValueMap = headers.get(NativeMessageHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
        if (multiValueMap != null && !multiValueMap.isEmpty()) {
            for (Map.Entry<String, List<String>> head : multiValueMap.entrySet()) {
                log.info("{}: {}", head.getKey(), head.getValue());
            }
        }
        return message;
    }
}
