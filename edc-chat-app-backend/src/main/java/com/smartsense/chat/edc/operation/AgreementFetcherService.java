package com.smartsense.chat.edc.operation;

import com.smartsense.chat.dao.entity.EdcProcessState;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.EdcProcessStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementFetcherService {

    private final EDCConnectorClient edc;
    private final AppConfig config;
    private final EdcProcessStateService edcProcessStateService;

    public String getAgreement(String negotiationId, EdcProcessState edcProcessState) {
        try {
            int count = 1;
            Map<String, Object> agreementResponse = null;
            String agreementId = null;
            do {
                Thread.sleep(10_000);
                log.info("Fetching agreement for negotiationId {}", negotiationId);
                agreementResponse = edc.getAgreement(config.edc().edcUri(),
                        negotiationId,
                        config.edc().authCode());
                log.info("AgreementResponse: {}", agreementResponse);
                if (!agreementResponse.get("state").toString().equals("FINALIZED")) {
                    count++;
                    continue;
                }
                agreementId = agreementResponse.get("contractAgreementId").toString();
                log.info("Negotiation {} is successfully done and agreementId is {}", negotiationId, agreementId);
                log.info("Fetching agreement for negotiationId {} is completed successfully.", negotiationId);
            } while (!agreementResponse.get("state").equals("FINALIZED") && count <= 3);
            return agreementId;
        } catch (Exception ex) {
            edcProcessState.setErrorDetail(String.format("Error occurred while getting agreement information for negotiationId %s and exception is %s", negotiationId, ex.getMessage()));
            edcProcessStateService.create(edcProcessState);
            log.error("Error occurred while getting agreement information for negotiationId {}", negotiationId, ex);
            return null;
        }
    }
}
