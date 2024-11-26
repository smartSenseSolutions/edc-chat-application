package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartsense.chat.edc.constant.EdcConstant.ACTION;
import static com.smartsense.chat.edc.constant.EdcConstant.CONTEXT;
import static com.smartsense.chat.edc.constant.EdcConstant.DCT;
import static com.smartsense.chat.edc.constant.EdcConstant.DCT_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC_NS_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.ID;
import static com.smartsense.chat.edc.constant.EdcConstant.ODRL_JSONLD_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.PERMISSION;
import static com.smartsense.chat.edc.constant.EdcConstant.TRACTUS_POLICY_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.TYPE;
import static com.smartsense.chat.edc.constant.EdcConstant.USE;
import static com.smartsense.chat.edc.constant.EdcConstant.VOCAB;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolicyCreationService {

    private final EDCConnectorClient edc;
    private final AppConfig config;

    public void createPolicy() {
        log.info("Policy creation process is started...");
        edc.createPolicy(config.edc().edcUri(), cretePolicyRequest(), config.edc().authCode());
        log.info("Policy creation process is completed...");
    }

    private Map<String, Object> cretePolicyRequest() {
        Map<String, Object> policyCreation = new HashMap<>();
        policyCreation.put(CONTEXT, List.of(TRACTUS_POLICY_URL, ODRL_JSONLD_URL,
                Map.of(VOCAB, EDC_NS_URL, DCT, DCT_URL)));
        policyCreation.put(TYPE, "PolicyDefinitionRequestDto");
        policyCreation.put(ID, config.edc().policyId());
        policyCreation.put("policy", Map.of(TYPE, "Set", PERMISSION, List.of(Map.of(ACTION, USE))));
        return policyCreation;
    }
}
