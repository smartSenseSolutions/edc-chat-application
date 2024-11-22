package com.smartsense.chat.edc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsense.chat.edc.manager.EDCProcessDto;
import com.smartsense.chat.edc.manager.ProcessManagerService;
import com.smartsense.chat.edc.operation.AgreementFetcherService;
import com.smartsense.chat.edc.operation.ContractNegotiationService;
import com.smartsense.chat.edc.operation.PublicUrlHandlerService;
import com.smartsense.chat.edc.operation.QueryCatalogService;
import com.smartsense.chat.edc.operation.TransferProcessService;
import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.utils.request.ChatMessage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private final ObjectMapper mapper;

    private final ProcessManagerService managerService;
    private final ProcessManagerService processManagerService;

    @SneakyThrows
    public List<Map> getChatHistory(String partnerBpn) {
        String history = """
                [
                    {
                        "receiver": "BPNL000000000001",
                        "sender": "BPNL000000000TATA",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000TATA",
                        "sender": "BPNL000000000001",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000001",
                        "sender": "BPNL000000000TATA",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000TATA",
                        "sender": "BPNL000000000001",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000001",
                        "sender": "BPNL000000000TATA",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000TATA",
                        "sender": "BPNL000000000001",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    }
                ]
                """;
        return mapper.readValue(history, List.class);
    }

    @Async
    public void initProcess(ChatMessage chatMessage) {

        EDCProcessDto processDto = processManagerService.getProcess(chatMessage.receiverBpn());

        if (Objects.nonNull(processDto) && StringUtils.hasText(processDto.getTransferProcessId())) {
            // Sent the message to public url
            publicUrlHandlerService.getAuthCodeAndPublicUrl(processDto.getTransferProcessId(), chatMessage);
            return;
        }

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

        prepareProcessDto(receiverBpnl, receiverDspUrl, offerId, agreementId, transferProcessId, negotiationId);
    }

    private void prepareProcessDto(String receiverBpnl, String receiverDspUrl, String offerId, String agreementId, String transferProcessId, String negotiationId) {
        managerService.put(receiverBpnl, EDCProcessDto.builder()
                .bpn(receiverBpnl)
                .dspUrl(receiverDspUrl)
                .offerId(offerId)
                .negotiationId(negotiationId)
                .agreementId(agreementId)
                .transferProcessId(transferProcessId)
                .build());
    }


}
