/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractDefinitionService {

    private final EDCConnectorClient edc;
    private final AppConfig config;

    public void createContractDefinition() {
        log.info("Contract Definition process is started...");
        edc.createContractDefinition(config.edc().edcUri(), createContractDefinitionRequest(), config.edc().authCode());
        log.info("Contract Definition process is completed...");
    }

    private Map<String, Object> createContractDefinitionRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("@context", Map.of("@vocab", "https://w3id.org/edc/v0.0.1/ns/"));
        request.put("@type", "ContractDefinition");
        request.put("@id", config.edc().contractDefinitionId());
        request.put("accessPolicyId", config.edc().policyId());
        request.put("contractPolicyId", config.edc().policyId());
        request.put("assetsSelector", Map.of("operandLeft", "https://w3id.org/edc/v0.0.1/ns/id", "operator", "=",
                "operandRight", config.edc().assetId()));
        return request;
    }
}
