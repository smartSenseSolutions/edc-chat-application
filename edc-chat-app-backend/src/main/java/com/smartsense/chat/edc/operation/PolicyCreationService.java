/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyCreationService {

    private final EDCConnectorClient edc;
    private final AppConfig config;

    public void createPolicy() {
        log.info("Policy creation process is started...");
        edc.createPolicy(config.edc().edcUri(), createMemberShipPolicyRequest(), config.edc().authCode());
        log.info("Policy creation process is completed...");
    }

    private Map<String, Object> cretePolicyRequest() {
        Map<String, Object> policyCreation = new HashMap<>();
        policyCreation.put("@context", List.of("https://w3id.org/tractusx/policy/v1.0.0", "http://www.w3.org/ns/odrl.jsonld",
                Map.of("@vocab", "https://w3id.org/edc/v0.0.1/ns/", "dct", "https://purl.org/dc/terms/")));
        policyCreation.put("@type", "PolicyDefinitionRequestDto");
        policyCreation.put("@id", config.edc().policyId());
        policyCreation.put("policy", Map.of("@type", "Set", "permission", List.of(Map.of("action", "use"))));
        return policyCreation;
    }

    private Map<String, Object> createMemberShipPolicyRequest() {
        Map<String, Object> policyCreation = new HashMap<>();
        policyCreation.put("@context", List.of("https://w3id.org/tractusx/policy/v1.0.0", "http://www.w3.org/ns/odrl.jsonld",
                Map.of("@vocab", "https://w3id.org/edc/v0.0.1/ns/", "dct", "https://purl.org/dc/terms/")));
        policyCreation.put("@type", "PolicyDefinitionRequestDto");
        policyCreation.put("@id", config.edc().policyId());
        policyCreation.put("policy", Map.of("@type", "Set", "permission", List.of(Map.of("action", "use",
                "constraint", Map.of("and", List.of(Map.of("leftOperand", "Membership", "operator", "eq", "rightOperand", "active")))))));
        return policyCreation;
    }
}
