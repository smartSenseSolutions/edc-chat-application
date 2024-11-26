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

import static com.smartsense.chat.edc.constant.EdcConstant.ACTION;
import static com.smartsense.chat.edc.constant.EdcConstant.CONTEXT;
import static com.smartsense.chat.edc.constant.EdcConstant.DATASPACE_PROTOCOL;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC_NS_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.ID;
import static com.smartsense.chat.edc.constant.EdcConstant.ODRL_JSONLD_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.PERMISSION;
import static com.smartsense.chat.edc.constant.EdcConstant.TRACTUS_POLICY_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.TYPE;
import static com.smartsense.chat.edc.constant.EdcConstant.USE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractNegotiationService {
    private final EDCConnectorClient edc;
    private final AppConfig config;
    private final ChatMessageService chatMessageService;

    private static List<Object> prepareNegotiationContext() {
        List<Object> context = new ArrayList<>();
        context.add(TRACTUS_POLICY_URL);
        context.add(ODRL_JSONLD_URL);
        context.add(Map.of(EDC, EDC_NS_URL));
        return context;
    }

    public String initNegotiation(String receiverDspUrl, String receiverBpnL, String offerId, ChatMessage chatMessage) {
        try {
            Map<String, Object> negotiationRequest = prepareNegotiationRequest(receiverDspUrl, receiverBpnL, offerId);
            log.info("Negotiation process is initiated for offerId {}", offerId);
            Map<String, Object> negotiationResponse = edc.initNegotiation(config.edc().edcUri(), negotiationRequest, config.edc().authCode());
            String negotiationId = negotiationResponse.get(ID).toString();
            log.info("Negotiation process is Competed for offerId {} with negotiationId {}", offerId, negotiationId);
            return negotiationId;
        } catch (Exception ex) {
            chatMessage.setErrorDetail(String.format("Error occurred while negotiating the contract offer %s with dspUrl %s and bpnl %s and Exception is %s.", offerId, receiverDspUrl, receiverBpnL, ex.getMessage()));
            chatMessageService.create(chatMessage);
            log.error("Error occurred while negotiating the contract offer {} with dspUrl {} and bpnl {}.", offerId, receiverDspUrl, receiverBpnL);
            return null;
        }
    }

    private Map<String, Object> prepareNegotiationRequest(String receiverDspUrl, String receiverBpnL, String offerId) {
        Map<String, Object> negotiationRequest = new HashMap<>();
        negotiationRequest.put(CONTEXT, prepareNegotiationContext());
        negotiationRequest.put(TYPE, "ContractRequest");
        negotiationRequest.put("edc:counterPartyAddress", receiverDspUrl);
        negotiationRequest.put("edc:protocol", DATASPACE_PROTOCOL);
        negotiationRequest.put("edc:counterPartyId", receiverBpnL);
        negotiationRequest.put("edc:policy", prepareNegotiationPolicy(receiverBpnL, offerId));
        log.info("Negotiation request looks like: {}", negotiationRequest);
        return negotiationRequest;
    }

    private Map<String, Object> prepareNegotiationPolicy(String receiverBpnL, String offerId) {
        Map<String, Object> negotiationPolicy = new HashMap<>();
        negotiationPolicy.put(ID, offerId);
        negotiationPolicy.put(TYPE, "Offer");
        negotiationPolicy.put(PERMISSION, List.of(Map.of(ACTION, USE)));
        negotiationPolicy.put("target", config.edc().assetId());
        negotiationPolicy.put("assigner", receiverBpnL);
        return negotiationPolicy;
    }
}
