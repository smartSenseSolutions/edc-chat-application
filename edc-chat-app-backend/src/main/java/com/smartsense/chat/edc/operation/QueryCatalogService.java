package com.smartsense.chat.edc.operation;

import com.smartsense.chat.dao.entity.EdcProcessState;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.EdcProcessStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryCatalogService {

    private final AppConfig config;
    private final EDCConnectorClient edc;
    private final EdcProcessStateService edcProcessStateService;

    public String queryCatalog(String receiverDspUrl, String receiverBpnl, EdcProcessState edcProcessState) {
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
                    .getString("@id");
            log.info("Received offerId {}.", offerId);
            return offerId;
        } catch (Exception ex) {
            edcProcessState.setErrorDetail(String.format("Error occurred while fetching the catalog for receiverDsp %s and Bpnl %s and exception is %s", receiverDspUrl, receiverBpnl, ex.getMessage()));
            edcProcessStateService.create(edcProcessState);
            log.error("Error occurred while fetching the catalog for receiverDsp {} and Bpnl {}", receiverDspUrl, receiverBpnl);
            return null;
        }
    }

    private Map<String, Object> prepareQueryCatalog(String counterPartyAddress, String counterPartyId, String assetId) {
        Map<String, Object> queryCatalog = new HashMap<>();
        queryCatalog.put("@context", Map.of("edc", "https://w3id.org/edc/v0.0.1/ns/", "odrl", "http://www.w3.org/ns/odrl/2/", "dct", "https://purl.org/dc/terms/"));
        queryCatalog.put("@type", "edc:CatalogRequest");
        queryCatalog.put("counterPartyAddress", counterPartyAddress);
        queryCatalog.put("counterPartyId", counterPartyId);
        queryCatalog.put("protocol", "dataspace-protocol-http");
        queryCatalog.put("querySpec", Map.of("filterExpression", List.of(Map.of("operandLeft", "https://w3id.org/edc/v0.0.1/ns/id",
                "operator", "=",
                "operandRight", assetId))));
        log.info("Create Query Catalog request looks like: {}", queryCatalog);
        return queryCatalog;
    }
}
