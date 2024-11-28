/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.operation;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.ChatMessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartsense.chat.utils.constant.ContField.TRANSFER_PROCESS_ID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferProcessService {

    private final EDCConnectorClient edc;
    private final AppConfig config;
    private final ChatMessageService chatMessageService;

    public String initiateTransfer(String agreementId, ChatMessage chatMessage) {
        try {
            log.info("Initiate transfer process for agreement Id {}", agreementId);

            // prepare transfer request
            Map<String, Object> transferRequest = prepareTransferRequest(agreementId);
            // initiate the transfer process
            Thread.sleep(5_000);
            List<Map<String, Object>> transferResponse = edc.initTransferProcess(config.edc().edcUri(), transferRequest, config.edc().authCode());
            log.info("Received transfer response -> {}", transferResponse);

            // get the transfer process id from response
            String transferProcessId = transferResponse.getFirst().get(TRANSFER_PROCESS_ID).toString();
            log.info("Transfer process id: {}", transferProcessId);

            log.info("Transfer process is complete successfully for agreement Id {}", agreementId);
            return transferProcessId;
        } catch (Exception ex) {
            chatMessage.setErrorDetail(String.format("Error occurred in transfer process for agreement Id %s and Exception is %s", agreementId, ex.getMessage()));
            chatMessageService.create(chatMessage);
            log.error("Error occurred in transfer process for agreement Id {}", agreementId, ex);
            return null;
        }
    }

    private Map<String, Object> prepareTransferRequest(String agreementId) {
        Map<String, Object> transferRequest = new HashMap<>();
        transferRequest.put("@context", Map.of("@vocab", "https://w3id.org/edc/v0.0.1/ns/"));
        transferRequest.put("@type", "QuerySpec");
        transferRequest.put("offset", 0);
        transferRequest.put("limit", 1);
        transferRequest.put("filterExpression", List.of(Map.of("operandLeft", "agreementId",
                "operator", "=",
                "operandRight", agreementId)));
        log.info("Transfer request looks like: {}", transferRequest);
        return transferRequest;
    }
}
