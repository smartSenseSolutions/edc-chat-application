package com.smartsense.chat.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket"); //the STOMP client connects to Websocket through this endpoint
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app"); // prefix for calling server methods
        config.enableSimpleBroker("/topic"); // prefix on which server will publish the message for client
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(rmeSessionChannelInterceptor()); // interceptor to obtain token through header
    }

    @Bean
    public WebSocketSessionChannelInterceptor rmeSessionChannelInterceptor() {
        return new WebSocketSessionChannelInterceptor();
    }
}
