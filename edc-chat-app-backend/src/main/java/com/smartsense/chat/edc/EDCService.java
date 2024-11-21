package com.smartsense.chat.edc;

import com.smartsense.chat.edc.operation.AgreementFetcherService;
import com.smartsense.chat.edc.operation.ContractNegotiationService;
import com.smartsense.chat.edc.operation.PublicUrlHandlerService;
import com.smartsense.chat.edc.operation.QueryCatalogService;
import com.smartsense.chat.edc.operation.TransferProcessService;
import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.utils.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EDCService {

    private final BusinessPartnerService partnerService;
    private final QueryCatalogService queryCatalogService;
    private final ContractNegotiationService contractNegotiationService;
    private final AgreementFetcherService agreementService;
    private final TransferProcessService transferProcessService;
    private final PublicUrlHandlerService publicUrlHandlerService;

    public void initProcess(ChatMessage chatMessage) {
        String receiverBpnl = chatMessage.receiverBpn();
        String receiverDspUrl = partnerService.getBusinessPartnerByBpn(receiverBpnl);
        // Query the catalog for chat asset
        String offerId = queryCatalogService.queryCatalog(receiverDspUrl, receiverBpnl);
        // Initiate the contract negotiation
        String negotiationId = contractNegotiationService.initNegotiation(receiverDspUrl, receiverBpnl, offerId);
        // Get agreement Id based on the negotiationId
        String agreementId = agreementService.getAgreement(negotiationId);
        // Initiate the transfer process
        String transferProcessId = transferProcessService.initiateTransfer(agreementId);
        // Sent the message to public url
        publicUrlHandlerService.getAuthCodeAndPublicUrl(transferProcessId, chatMessage);
    }


}
