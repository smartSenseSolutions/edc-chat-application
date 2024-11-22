package com.smartsense.chat.edc.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.utils.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicUrlHandlerService {
    private final EDCConnectorClient edc;
    private final ObjectMapper mapper;
    private final AppConfig config;

    public void getAuthCodeAndPublicUrl(String transferProcessId, ChatMessage message) {
        try {
            log.info("Initiate to get auth code based on transfer process id " + transferProcessId);
            Map<String, Object> response = edc.getAuthCodeAndPublicUrl(config.edc().edcUri(), transferProcessId, config.edc().authCode());
            log.info("Auth code and public url response -> {}", response);

            // Retrieve public path and authorization code
            String publicPath = response.get("tx-auth:refreshEndpoint").toString();
            String authorization = response.get("authorization").toString();

            // Call the public path with authorization code
            callPublicUri(publicPath, mapper.writeValueAsString(message), authorization);

            log.info("Initiate to get auth code based on transfer process id {} is done.", transferProcessId);
        } catch (Exception ex) {
            log.error("Error occurred in get auth code based on transfer process id {} ", transferProcessId, ex);
        }
    }

    private void callPublicUri(String publicPath, String message, String authorization) {
        try {
            Map<String, Object> publicUriResponse = edc.sendMessage(URI.create(publicPath), message, authorization);
            log.info("Received public uri response -> {}", publicUriResponse);
        } catch (Exception ex) {
            log.error("Error occurred while calling public uri {} and auth code {} ", publicPath, authorization, ex);
        }
    }
}
