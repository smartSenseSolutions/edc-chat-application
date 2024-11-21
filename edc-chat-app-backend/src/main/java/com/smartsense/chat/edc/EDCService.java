package com.smartsense.chat.edc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsense.chat.edc.operation.AgreementFetcherService;
import com.smartsense.chat.edc.operation.ContractNegotiationService;
import com.smartsense.chat.edc.operation.PublicUrlHandlerService;
import com.smartsense.chat.edc.operation.QueryCatalogService;
import com.smartsense.chat.edc.operation.TransferProcessService;
import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.utils.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EDCService {

    private final ObjectMapper mapper;
    private final BusinessPartnerService partnerService;
    private final QueryCatalogService queryCatalogService;
    private final ContractNegotiationService contractNegotiationService;
    private final AgreementFetcherService agreementService;
    private final TransferProcessService transferProcessService;
    private final PublicUrlHandlerService publicUrlHandlerService;

    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void initProcess() {
        ChatMessage message = new ChatMessage("BPNL000000000000",
                "BPNL000000000001",
                new JSONObject(Map.of("Hello", "My dear dost..")).toString(),
                UUID.randomUUID().toString());
        System.out.println(mapper.writeValueAsString(message));
    }

    @Async
    public void initProcess(ChatMessage chatMessage) {
        String receiverBpnl = chatMessage.receiverBpn();
        String receiverDspUrl = partnerService.getBusinessPartnerByBpn(receiverBpnl);
        // Query the catalog for chat asset
        String offerId = queryCatalogService.queryCatalog(receiverDspUrl, receiverBpnl);
        if (!StringUtils.hasText(offerId)) {
            log.error("Not able to retrieve the offerId from EDC {}, please check manually.", receiverDspUrl);
            return;
        }
        // Initiate the contract negotiation
        String negotiationId = contractNegotiationService.initNegotiation(receiverDspUrl, receiverBpnl, offerId);
        if (!StringUtils.hasText(negotiationId)) {
            log.error("Not able to initiate the negotiation for EDC {} and offerId {}, please check manually.", receiverDspUrl, offerId);
            return;
        }
        // Get agreement Id based on the negotiationId
        String agreementId = agreementService.getAgreement(negotiationId);
        if (!StringUtils.hasText(agreementId)) {
            log.error("Not able to get the agreement for offerId {} and negotiationId {}, please check manually.", offerId, negotiationId);
            return;
        }
        // Initiate the transfer process
        String transferProcessId = transferProcessService.initiateTransfer(agreementId);
        if (!StringUtils.hasText(transferProcessId)) {
            log.error("Not able to get the agreement for transferProcessId {}, please check manually.", transferProcessId);
            return;
        }
        // Sent the message to public url
        publicUrlHandlerService.getAuthCodeAndPublicUrl(transferProcessId, chatMessage);
    }


}
