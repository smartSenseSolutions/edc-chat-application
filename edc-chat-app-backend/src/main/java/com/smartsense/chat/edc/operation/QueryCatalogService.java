package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.EDCConfigurations;
import com.smartsense.chat.service.BusinessPartnerService;
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

    private final EDCConfigurations edcConfigurations;
    private final BusinessPartnerService partnerService;
    private final EDCConnectorClient edc;

    public String queryCatalog(String receiverDspUrl, String receiverBpnl) {
        try {
            log.info("Creating Query Catalog process started...");
            Map<String, Object> response = edc.queryCatalog(edcConfigurations.edcUri(), prepareQueryCatalog(receiverDspUrl, receiverBpnl, edcConfigurations.assetId()), edcConfigurations.authCode());
            JSONObject catalogResponse = new JSONObject(response);
            JSONObject dataSet = catalogResponse.getJSONObject("dcat:dataset");
            if (dataSet.isEmpty()) {
                log.info("No data set found for the assetId {} from dsp {}", edcConfigurations.assetId(), receiverDspUrl);
                return null;
            }
            String offerId = dataSet
                    .getJSONObject("odrl:hasPolicy")
                    .getString("@id");
            log.info("Received offerId {}.", offerId);
            return offerId;
        } catch (Exception ex) {
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
