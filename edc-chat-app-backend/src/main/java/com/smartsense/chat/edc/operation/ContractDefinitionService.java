package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.smartsense.chat.edc.constant.EdcConstant.CONTEXT;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC_NS_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.EQUAL;
import static com.smartsense.chat.edc.constant.EdcConstant.ID;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERAND_LEFT;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERAND_RIGHT;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERATOR;
import static com.smartsense.chat.edc.constant.EdcConstant.TYPE;
import static com.smartsense.chat.edc.constant.EdcConstant.VOCAB;

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
        request.put(CONTEXT, Map.of(VOCAB, EDC_NS_URL));
        request.put(TYPE, "ContractDefinition");
        request.put(ID, config.edc().contractDefinitionId());
        request.put("accessPolicyId", config.edc().policyId());
        request.put("contractPolicyId", config.edc().policyId());
        request.put("assetsSelector", Map.of(OPERAND_LEFT, "https://w3id.org/edc/v0.0.1/ns/id", OPERATOR, EQUAL,
                OPERAND_RIGHT, config.edc().assetId()));
        return request;
    }
}
