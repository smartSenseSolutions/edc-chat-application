package com.smartsense.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    /**
     * Configures the message broker for WebSocket communication.
     * This method sets up the destinations for message routing and handling.
     *
     * @param registry The MessageBrokerRegistry to be configured.
     *                 This registry is used to enable and configure various
     *                 aspects of the message broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }


    /**
     * Registers STOMP endpoints for WebSocket communication.
     * This method configures the endpoint that the clients will use to connect to the WebSocket server.
     *
     * @param stompEndpointRegistry The StompEndpointRegistry to be configured.
     *                              This registry is used to register STOMP endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*");
    }
}
