package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.smartsense.chat.edc.constant.EdcConstant.CONTEXT;
import static com.smartsense.chat.edc.constant.EdcConstant.DCT;
import static com.smartsense.chat.edc.constant.EdcConstant.DCT_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC_NS_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.ID;
import static com.smartsense.chat.edc.constant.EdcConstant.S_TRUE;
import static com.smartsense.chat.edc.constant.EdcConstant.S_TYPE;
import static com.smartsense.chat.edc.constant.EdcConstant.TYPE;
import static com.smartsense.chat.web.ApiConstant.RECEIVE_CHAT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetCreationService {

    private final EDCConnectorClient edc;
    private final AppConfig config;
    @Value("${spring.application.host:http://localhost:8080}")
    private String host;


    public void createAsset() {
        log.info("Asset Creation process is started..");
        edc.createAsset(config.edc().edcUri(), prepareAssetCreationRequest(), config.edc().authCode());
        log.info("Asset Creation process is completed..");
    }

    private Map<String, Object> prepareAssetCreationRequest() {
        String baseUrl = host.concat(RECEIVE_CHAT);
        Map<String, Object> assetCreation = new HashMap<>();
        assetCreation.put(CONTEXT, Map.of(EDC, EDC_NS_URL,
                "cx-common", "https://w3id.org/catenax/ontology/common#",
                "cx-taxo", "https://w3id.org/catenax/taxonomy#",
                DCT, DCT_URL));
        assetCreation.put(ID, config.edc().assetId());
        assetCreation.put("properties", Map.of(S_TYPE, Map.of(ID, "Asset")));
        assetCreation.put("dataAddress", Map.of(TYPE, "DataAddress", S_TYPE, "HttpData",
                "baseUrl", baseUrl, "proxyQueryParams", S_TRUE, "proxyPath", S_TRUE,
                "proxyMethod", S_TRUE, "proxyBody", S_TRUE, "method", "POST"));
        log.trace("Create Asset Creation request looks like: {}", assetCreation);
        return assetCreation;
    }

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
}
