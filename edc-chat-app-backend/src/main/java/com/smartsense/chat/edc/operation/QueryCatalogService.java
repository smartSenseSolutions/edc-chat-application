package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.EDCConfigurations;
import com.smartsense.chat.service.BusinessPartnerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryCatalogService {

    private final EDCConfigurations configurations;
    private final BusinessPartnerService partnerService;
    private final EDCConnectorClient edc;

    public void QueryCatalog(@NotNull String bpn) {
        log.info("Creating Query Catalog process started...");
        // TODO fetching edcUrl by bpn
        String edcUrl = partnerService.getBusinessPartnerByBpn(bpn);
        Map<String, Object> request = prepareQueryCatalog(edcUrl, bpn, configurations.assetId());
        Map<String, Object> response = edc.queryCatalog(URI.create(edcUrl), request, configurations.authCode());
        log.info("Response of catalog creation: {}", response);

        // TODO handle and process response
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
