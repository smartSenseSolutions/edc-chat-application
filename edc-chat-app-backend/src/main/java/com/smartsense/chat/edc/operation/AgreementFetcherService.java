package com.smartsense.chat.edc.operation;

import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.EDCConfigurations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementFetcherService {

    private final EDCConnectorClient edc;
    private final EDCConfigurations edcConfigurations;

    public String getAgreement(String negotiationId) {
        try {
            int count = 1;
            Map<String, Object> agreementResponse = null;
            String agreementId = null;
            do {
                Thread.sleep(5_000);
                log.info("Fetching agreement for negotiationId {}", negotiationId);
                agreementResponse = edc.getAgreement(edcConfigurations.edcUri(), negotiationId, edcConfigurations.authCode());
                if (!agreementResponse.get("state").toString().equals("FINALIZED")) {
                    count++;
                    continue;
                }
                agreementId = agreementResponse.get("contractAgreementId").toString();
                log.info("Negotiation {} is successfully done and agreementId is {}", negotiationId, agreementId);
                log.info("Fetching agreement for negotiationId {} is completed successfully.", negotiationId);
            } while ((!CollectionUtils.isEmpty(agreementResponse) && agreementResponse.get("state").equals("FINALIZED")) ||
                    StringUtils.hasText(agreementId) ||
                    count == 3);
            return agreementId;
        } catch (Exception ex) {
            log.error("Error occurred while getting agreement information for negotiationId {}", negotiationId, ex);
            return null;
        }
    }
}
