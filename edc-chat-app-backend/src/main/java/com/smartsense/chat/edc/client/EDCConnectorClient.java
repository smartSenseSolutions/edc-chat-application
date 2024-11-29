/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "edc", url = "http://localhost:8182")
public interface EDCConnectorClient {

    @PostMapping(value = "/management/v3/assets", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    void createAsset(URI baseUri,
                     @RequestBody Map<String, Object> request,
                     @RequestHeader("X-Api-Key") String auth);

    @GetMapping(value = "/management/v3/assets/{assetId}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getAsset(URI baseUri,
                                    @PathVariable("assetId") String assetId,
                                    @RequestHeader("X-Api-Key") String auth);

    @PostMapping(value = "/management/v2/policydefinitions", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    void createPolicy(URI baseUri,
                      @RequestBody Map<String, Object> request,
                      @RequestHeader("X-Api-Key") String auth);

    @PostMapping(value = "/management/v2/contractdefinitions", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    void createContractDefinition(URI baseUri,
                                  @RequestBody Map<String, Object> request,
                                  @RequestHeader("X-Api-Key") String auth);

    @PostMapping(value = "/management/v2/catalog/request", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Map<String, Object> queryCatalog(URI baseUri,
                                     @RequestBody Map<String, Object> request,
                                     @RequestHeader("X-Api-Key") String auth);

    @PostMapping(value = "/management/v3/contractnegotiations", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Map<String, Object> initNegotiationWithoutEDR(URI baseUri,
                                                  @RequestBody Map<String, Object> request,
                                                  @RequestHeader("X-Api-Key") String auth);

    @PostMapping(value = "/management/v2/edrs", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Map<String, Object> initNegotiation(URI baseUri,
                                        @RequestBody Map<String, Object> request,
                                        @RequestHeader("X-Api-Key") String auth);

    @GetMapping(value = "/management/v2/contractnegotiations/{negotiationId}", produces = APPLICATION_JSON_VALUE)
    Map<String, Object> getAgreement(URI baseUri,
                                     @PathVariable("negotiationId") String negotiationId,
                                     @RequestHeader("X-Api-Key") String auth);


    @PostMapping(value = "/management/v2/edrs/request", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    List<Map<String, Object>> initTransferProcess(URI baseUri,
                                                  @RequestBody Map<String, Object> request,
                                                  @RequestHeader("X-Api-Key") String auth);

    @PostMapping(value = "/management/v2/transferprocesses", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Map<String, Object> initTransferProcessWithoutEDR(URI baseUri,
                                                      @RequestBody Map<String, Object> request,
                                                      @RequestHeader("X-Api-Key") String auth);

    @GetMapping(value = "management/v2/edrs/{transferProcessId}/dataaddress", produces = APPLICATION_JSON_VALUE)
    Map<String, Object> getAuthCodeAndPublicUrl(URI baseUri,
                                                @PathVariable("transferProcessId") String transferProcessId,
                                                @RequestHeader("X-Api-Key") String auth);

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Map<String, Object> sendMessage(URI baseUri, @RequestBody String message, @RequestHeader("Authorization") String authorization);

}
