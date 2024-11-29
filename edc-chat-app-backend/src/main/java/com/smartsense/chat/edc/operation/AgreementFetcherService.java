/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.operation;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.ChatMessageService;

import java.util.Map;

import static com.smartsense.chat.utils.constant.ContField.AGREEMENT_STATE;
import static com.smartsense.chat.utils.constant.ContField.AGREEMENT_STATE_FINALIZED;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementFetcherService {

    private final EDCConnectorClient edc;
    private final AppConfig config;
    private final ChatMessageService chatMessageService;

    public String getAgreement(String negotiationId, ChatMessage chatMessage) {
        try {
            int count = 1;
            Map<String, Object> agreementResponse;
            String agreementId = null;
            do {
                Thread.sleep(Long.parseLong(config.edc().agreementWaitTime()));
                log.info("Fetching agreement for negotiationId {}", negotiationId);
                agreementResponse = edc.getAgreement(config.edc().edcUri(),
                        negotiationId,
                        config.edc().authCode());
                log.info("AgreementResponse: {}", agreementResponse);
                if (!agreementResponse.get(AGREEMENT_STATE).toString().equals(AGREEMENT_STATE_FINALIZED)) {
                    count++;
                    continue;
                }
                agreementId = agreementResponse.get("contractAgreementId").toString();
                log.info("Negotiation {} is successfully done and agreementId is {}", negotiationId, agreementId);
                log.info("Fetching agreement for negotiationId {} is completed successfully.", negotiationId);
            } while (!agreementResponse.get(AGREEMENT_STATE).equals(AGREEMENT_STATE_FINALIZED) && count <= config.edc().agreementRetryLimit());
            return agreementId;
        } catch (Exception ex) {
            chatMessage.setErrorDetail(String.format("Error occurred while getting agreement information for negotiationId %s and exception is %s", negotiationId, ex.getMessage()));
            chatMessageService.create(chatMessage);
            log.error("Error occurred while getting agreement information for negotiationId {}", negotiationId, ex);
            return null;
        }
    }
}
