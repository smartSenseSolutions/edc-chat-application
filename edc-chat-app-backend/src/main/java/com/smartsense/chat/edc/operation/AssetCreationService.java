package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.smartsense.chat.web.ApiConstant.RECEIVE_CHAT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetCreationService {

    private final EDCConnectorClient edc;
    private final AppConfig config;
    @Value("${spring.application.host}")
    private String host;


    public void createAsset() {
        log.info("Asset Creation process is started..");
        edc.createAsset(config.edc().edcUri(), prepareAssetCreationRequest(), config.edc().authCode());
        log.info("Asset Creation process is completed..");
    }

    private Map<String, Object> prepareAssetCreationRequest() {
        String baseUrl = String.format(host + RECEIVE_CHAT);
        Map<String, Object> assetCreation = new HashMap<>();
        assetCreation.put("@context", Map.of("edc", "https://w3id.org/edc/v0.0.1/ns/",
                "cx-common", "https://w3id.org/catenax/ontology/common#",
                "cx-taxo", "https://w3id.org/catenax/taxonomy#",
                "dct", "https://purl.org/dc/terms/"));
        assetCreation.put("@id", config.edc().assetId());
        assetCreation.put("properties", Map.of("type", Map.of("@id", "Asset")));
        assetCreation.put("dataAddress", Map.of("@type", "DataAddress", "type", "HttpData",
                "baseUrl", baseUrl, "proxyQueryParams", "true", "proxyPath", "true",
                "proxyMethod", "true", "proxyBody", "true", "method", "POST"));
        log.info("Create Asset Creation request looks like: {}", assetCreation);
        return assetCreation;
    }

    public boolean checkAssetPresent() {
        ResponseEntity<String> response = edc.getAsset(config.edc().edcUri(), config.edc().assetId(), config.edc().authCode());
        return response.getStatusCode().is2xxSuccessful();
    }
}
