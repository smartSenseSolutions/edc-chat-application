/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;

import java.util.HashMap;
import java.util.Map;

import static com.smartsense.chat.web.ApiConstant.RECEIVE_CHAT;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetCreationService {

    private final EDCConnectorClient edc;
    private final AppConfig config;
    @Value("${spring.application.host:http://localhost:8080}")
    private String host;


    /**
     * Creates an asset using the EDC connector client.
     * <p>
     * This method initiates the asset creation process, logs the start and completion
     * of the process, and uses the EDC connector client to create the asset with
     * the prepared asset creation request.
     * <p>
     * The method does not take any parameters as it uses the injected EDC connector client
     * and configuration to perform the asset creation.
     */
    public void createAsset() {
        log.info("Asset Creation process is started..");
        edc.createAsset(config.edc().edcUri(), prepareAssetCreationRequest(), config.edc().authCode());
        log.info("Asset Creation process is completed..");
    }

    /**
     * Checks if an asset is present in the EDC system.
     * <p>
     * This method attempts to retrieve an asset using the EDC connector client.
     * It uses the configured EDC URI, asset ID, and authentication code to make the request.
     *
     * @return boolean indicating whether the asset is present
     * true if the asset is successfully retrieved (HTTP 2xx response)
     * false if the asset retrieval fails or an exception occurs
     */
    public boolean checkAssetPresent() {
        boolean success = false;
        try {
            ResponseEntity<Object> response = edc.getAsset(config.edc().edcUri(), config.edc().assetId(), config.edc().authCode());
            success = response.getStatusCode().is2xxSuccessful();
        } catch (FeignException e) {
            log.info("Feign  ERROR :{}", e.getMessage());
        }
        return success;
    }


    private Map<String, Object> prepareAssetCreationRequest() {
        String baseUrl = host.concat(RECEIVE_CHAT);
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

}
