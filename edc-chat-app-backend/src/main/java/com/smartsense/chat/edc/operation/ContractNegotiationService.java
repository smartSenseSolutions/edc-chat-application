/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.operation;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractNegotiationService {
    private final EDCConnectorClient edc;
    private final AppConfig config;
    private final ChatMessageService chatMessageService;

    private static List<Object> prepareNegotiationContext() {
        List<Object> context = new ArrayList<>();
        context.add("https://w3id.org/tractusx/policy/v1.0.0");
        context.add("http://www.w3.org/ns/odrl.jsonld");
        context.add(Map.of("edc", "https://w3id.org/edc/v0.0.1/ns/"));
        return context;
    }

    public String initNegotiation(String receiverDspUrl, String receiverBpnL, String offerId, ChatMessage chatMessage) {
        try {
            log.info("Starting negotiation process with bpnl {}, dspUrl {} and offerId {}", receiverBpnL, receiverDspUrl, offerId);
            Map<String, Object> negotiationRequest = prepareNegotiationRequest(receiverDspUrl, receiverBpnL, offerId);
            log.info("Negotiation initiated for offerId {}", offerId);
            Map<String, Object> negotiationResponse = edc.initNegotiation(config.edc().edcUri(), negotiationRequest, config.edc().authCode());
            String negotiationId = negotiationResponse.get("@id").toString();
            log.info("Contract negotiation process done for offerId {} with negotiationId {}", offerId, negotiationId);
            return negotiationId;
        } catch (Exception ex) {
            chatMessage.setErrorDetail(String.format("Error occurred while negotiating the contract offer %s with dspUrl %s and bpnl %s and Exception is %s.", offerId, receiverDspUrl, receiverBpnL, ex.getMessage()));
            chatMessageService.create(chatMessage);
            log.error("Error occurred while negotiating the contract offer {} with dspUrl {} and bpnl {}.", offerId, receiverDspUrl, receiverBpnL);
            return null;
        }
    }

    public String initNegotiationWithoutEDR(String receiverDspUrl, String receiverBpnL, String offerId, ChatMessage chatMessage) {
        try {
            log.info("Starting negotiation(Without EDR) process with bpnl {}, dspUrl {} and offerId {}", receiverBpnL, receiverDspUrl, offerId);
            Map<String, Object> negotiationRequest = prepareNegotiationRequestWithoutEDR(receiverDspUrl, receiverBpnL, offerId, config.edc().assetId());
            log.info("Negotiation(Without EDR) initiated for offerId {}", offerId);
            Map<String, Object> negotiationResponse = edc.initNegotiationWithoutEDR(config.edc().edcUri(), negotiationRequest, config.edc().authCode());
            String negotiationId = negotiationResponse.get("@id").toString();
            log.info("Contract negotiation(Without EDR) process done for offerId {} with negotiationId {}", offerId, negotiationId);
            return negotiationId;
        } catch (Exception ex) {
            chatMessage.setErrorDetail(String.format("Error occurred while negotiating(Without EDR) the contract offer %s with dspUrl %s and bpnl %s and Exception is %s.", offerId, receiverDspUrl, receiverBpnL, ex.getMessage()));
            chatMessageService.create(chatMessage);
            log.error("Error occurred while negotiating(Without EDR) the contract offer {} with dspUrl {} and bpnl {}.", offerId, receiverDspUrl, receiverBpnL);
            return null;
        }
    }

    private Map<String, Object> prepareNegotiationRequest(String receiverDspUrl, String receiverBpnL, String offerId) {
        Map<String, Object> negotiationRequest = new HashMap<>();
        negotiationRequest.put("@context", prepareNegotiationContext());
        negotiationRequest.put("@type", "ContractRequest");
        negotiationRequest.put("edc:counterPartyAddress", receiverDspUrl);
        negotiationRequest.put("edc:protocol", "dataspace-protocol-http");
        negotiationRequest.put("edc:counterPartyId", receiverBpnL);
        negotiationRequest.put("edc:policy", prepareNegotiationPolicy(receiverBpnL, offerId));
        log.info("Negotiation request looks like: {}", negotiationRequest);
        return negotiationRequest;
    }

    private Map<String, Object> prepareNegotiationPolicy(String receiverBpnL, String offerId) {
        Map<String, Object> negotiationPolicy = new HashMap<>();
        negotiationPolicy.put("@id", offerId);
        negotiationPolicy.put("@type", "Offer");
        negotiationPolicy.put("permission", List.of(Map.of("action", "use")));
        negotiationPolicy.put("target", config.edc().assetId());
        negotiationPolicy.put("assigner", receiverBpnL);
        return negotiationPolicy;
    }

    private Map<String, Object> prepareNegotiationRequestWithoutEDR(String receiverDspUrl, String receiverBpnL, String offerId, String assetId) {
        Map<String, Object> negotiationRequest = new HashMap<>();
        negotiationRequest.put("@context", Map.of("@vocab", "https://w3id.org/edc/v0.0.1/ns/",
                "odrl", "http://www.w3.org/ns/odrl/2/"));
        negotiationRequest.put("counterPartyAddress", receiverDspUrl);
        negotiationRequest.put("counterPartyId", receiverBpnL);
        negotiationRequest.put("protocol", "dataspace-protocol-http");
        negotiationRequest.put("policy", prepareNegotiationPolicyWithoutEDR(offerId, receiverBpnL, assetId));
        log.info("Negotiation request without EDR looks like: {}", negotiationRequest);
        return negotiationRequest;
    }

    private Map<String, Object> prepareNegotiationPolicyWithoutEDR(String offerId, String receiverBpnL, String assetId) {
        Map<String, Object> policy = new HashMap<>();
        policy.put("@context", "http://www.w3.org/ns/odrl.jsonld");
        policy.put("@id", offerId);
        policy.put("@type", "Offer");
        policy.put("assigner", receiverBpnL);
        policy.put("target", assetId);
        policy.put("odrl:permission", List.of(Map.of("action", "use")));
        return policy;
    }
}
