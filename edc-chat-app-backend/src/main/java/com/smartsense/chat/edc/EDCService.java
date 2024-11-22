package com.smartsense.chat.edc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.dao.entity.EdcProcessState;
import com.smartsense.chat.edc.operation.AgreementFetcherService;
import com.smartsense.chat.edc.operation.ContractNegotiationService;
import com.smartsense.chat.edc.operation.PublicUrlHandlerService;
import com.smartsense.chat.edc.operation.QueryCatalogService;
import com.smartsense.chat.edc.operation.TransferProcessService;
import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.service.ChatMessageService;
import com.smartsense.chat.service.EdcProcessStateService;
import com.smartsense.chat.utils.request.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final EdcProcessStateService edcProcessStateService;
    private final ChatMessageService chatMessageService;
    private final ObjectMapper mapper;

    @Async
    public void initProcess(ChatRequest chatMessage) {
        String receiverBpnl = chatMessage.receiverBpn();
        EdcProcessState educByReceiverBpn = edcProcessStateService.getEdcByBpn(receiverBpnl);
        ChatMessage chatResponse = chatMessageService.createChat(chatMessage, true, false);
        if (Objects.nonNull(educByReceiverBpn) && StringUtils.hasText(educByReceiverBpn.getTransferId())) {
            chatMessageService.updateChat(chatResponse, false, educByReceiverBpn);
            publicUrlHandlerService.getAuthCodeAndPublicUrl(educByReceiverBpn.getTransferId(), chatMessage, educByReceiverBpn);
            chatMessageService.updateChat(chatResponse, true, null);
            return;
        }

        EdcProcessState edcProcessState = new EdcProcessState();
        edcProcessState.setReceiverBpn(chatMessage.receiverBpn());
        String receiverDspUrl = partnerService.getBusinessPartnerByBpn(receiverBpnl);

        // Query the catalog for chat asset
        String offerId = queryCatalogService.queryCatalog(receiverDspUrl, receiverBpnl, edcProcessState);
        if (!StringUtils.hasText(offerId)) {
            log.error("Not able to retrieve the offerId from EDC {}, please check manually.", receiverDspUrl);
            return;
        }
        edcProcessState = setAndSaveEdcState("OfferId", offerId, edcProcessState);
        chatMessageService.updateChat(chatResponse, false, edcProcessState);
        // Initiate the contract negotiation
        String negotiationId = contractNegotiationService.initNegotiation(receiverDspUrl, receiverBpnl, offerId, edcProcessState);
        if (!StringUtils.hasText(negotiationId)) {
            log.error("Not able to initiate the negotiation for EDC {} and offerId {}, please check manually.", receiverDspUrl, offerId);
            return;
        }
        edcProcessState = setAndSaveEdcState("NegotiationId", negotiationId, edcProcessState);

        // Get agreement Id based on the negotiationId
        String agreementId = agreementService.getAgreement(negotiationId, edcProcessState);
        if (!StringUtils.hasText(agreementId)) {
            log.error("Not able to get the agreement for offerId {} and negotiationId {}, please check manually.", offerId, negotiationId);
            return;
        }
        edcProcessState = setAndSaveEdcState("AgreementId", agreementId, edcProcessState);

        // Initiate the transfer process
        String transferProcessId = transferProcessService.initiateTransfer(agreementId, edcProcessState);
        if (!StringUtils.hasText(transferProcessId)) {
            log.error("Not able to get the agreement for transferProcessId {}, please check manually.", transferProcessId);
            return;
        }
        edcProcessState = setAndSaveEdcState("TransferId", transferProcessId, edcProcessState);

        // Sent the message to public url
        publicUrlHandlerService.getAuthCodeAndPublicUrl(transferProcessId, chatMessage, edcProcessState);
        chatMessageService.updateChat(chatResponse, true, null);
    }

    private EdcProcessState setAndSaveEdcState(String fieldName, String value, EdcProcessState edcProcessState) {
        switch (fieldName) {
            case "OfferId" -> edcProcessState.setOfferId(value);
            case "NegotiationId" -> edcProcessState.setNegotiationId(value);
            case "AgreementId" -> edcProcessState.setAgreementId(value);
            case "TransferId" -> edcProcessState.setTransferId(value);
            default -> throw new IllegalArgumentException("Unsupported field name: " + fieldName);
        }
        return edcProcessStateService.create(edcProcessState);
    }

    public void receiveMessage(ChatRequest message) {
        log.info("Received message: {}", message);
        chatMessageService.createChat(message, false, true);
    }

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
}
