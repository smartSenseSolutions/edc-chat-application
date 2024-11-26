package com.smartsense.chat.edc.operation;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartsense.chat.edc.constant.EdcConstant.CONTEXT;
import static com.smartsense.chat.edc.constant.EdcConstant.COUNTER_PARTY_ADDRESS;
import static com.smartsense.chat.edc.constant.EdcConstant.COUNTER_PARTY_ID;
import static com.smartsense.chat.edc.constant.EdcConstant.DATASPACE_PROTOCOL;
import static com.smartsense.chat.edc.constant.EdcConstant.DCT;
import static com.smartsense.chat.edc.constant.EdcConstant.DCT_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC_NS_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.EQUAL;
import static com.smartsense.chat.edc.constant.EdcConstant.FILTER_EXPRESSION;
import static com.smartsense.chat.edc.constant.EdcConstant.ID;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERAND_LEFT;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERAND_RIGHT;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERATOR;
import static com.smartsense.chat.edc.constant.EdcConstant.PROTOCOL;
import static com.smartsense.chat.edc.constant.EdcConstant.TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryCatalogService {

    private final AppConfig config;
    private final EDCConnectorClient edc;
    private final ChatMessageService chatMessageService;

    public String queryCatalog(String receiverDspUrl, String receiverBpnl, ChatMessage chatMessage) {
        try {
            log.info("Creating Query Catalog process started...");
            Map<String, Object> response = edc.queryCatalog(config.edc().edcUri(), prepareQueryCatalog(receiverDspUrl, receiverBpnl, config.edc().assetId()), config.edc().authCode());
            JSONObject catalogResponse = new JSONObject(response);
            JSONObject dataSet = catalogResponse.getJSONObject("dcat:dataset");
            if (dataSet.isEmpty()) {
                log.info("No data set found for the assetId {} from dsp {}", config.edc().assetId(), receiverDspUrl);
                return null;
            }
            String offerId = dataSet
                    .getJSONObject("odrl:hasPolicy")
                    .getString(ID);
            log.info("Received offerId {}.", offerId);
            return offerId;
        } catch (Exception ex) {
            chatMessage.setErrorDetail(String.format("Error occurred while fetching the catalog for receiverDsp %s and Bpnl %s and exception is %s", receiverDspUrl, receiverBpnl, ex.getMessage()));
            chatMessageService.create(chatMessage);
            log.error("Error occurred while fetching the catalog for receiverDsp {} and Bpnl {}", receiverDspUrl, receiverBpnl);
            return null;
        }
    }

    private Map<String, Object> prepareQueryCatalog(String counterPartyAddress, String counterPartyId, String assetId) {
        Map<String, Object> queryCatalog = new HashMap<>();
        queryCatalog.put(CONTEXT, Map.of(EDC, EDC_NS_URL, "odrl", "http://www.w3.org/ns/odrl/2/", DCT, DCT_URL));
        queryCatalog.put(TYPE, "edc:CatalogRequest");
        queryCatalog.put(COUNTER_PARTY_ADDRESS, counterPartyAddress);
        queryCatalog.put(COUNTER_PARTY_ID, counterPartyId);
        queryCatalog.put(PROTOCOL, DATASPACE_PROTOCOL);
        queryCatalog.put("querySpec", Map.of(FILTER_EXPRESSION, List.of(Map.of(OPERAND_LEFT, "https://w3id.org/edc/v0.0.1/ns/id",
                OPERATOR, EQUAL,
                OPERAND_RIGHT, assetId))));
        log.info("Create Query Catalog request looks like: {}", queryCatalog);
        return queryCatalog;
    }
}
