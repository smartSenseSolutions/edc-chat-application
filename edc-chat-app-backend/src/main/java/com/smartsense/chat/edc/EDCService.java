package com.smartsense.chat.edc;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.dao.entity.EdcProcessState;
import com.smartsense.chat.dao.repository.ChatMessageRepository;
import com.smartsense.chat.edc.operation.AgreementFetcherService;
import com.smartsense.chat.edc.operation.AssetCreationService;
import com.smartsense.chat.edc.operation.ContractDefinitionService;
import com.smartsense.chat.edc.operation.ContractNegotiationService;
import com.smartsense.chat.edc.operation.PolicyCreationService;
import com.smartsense.chat.edc.operation.PublicUrlHandlerService;
import com.smartsense.chat.edc.operation.QueryCatalogService;
import com.smartsense.chat.edc.operation.TransferProcessService;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.service.ChatMessageService;
import com.smartsense.chat.service.EdcProcessStateService;
import com.smartsense.chat.utils.request.ChatRequest;
import com.smartsense.chat.utils.response.ChatHistoryResponse;
import com.smartsense.chat.utils.response.MessageStatus;
import com.smartsense.chat.utils.validate.Validate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.smartsense.chat.utils.constant.ContField.AGREEMENT_ID;
import static com.smartsense.chat.utils.constant.ContField.NEGOTIATION_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EDCService {

    private final ChatMessageRepository chatMessageRepository;
    private final BusinessPartnerService partnerService;
    private final QueryCatalogService queryCatalogService;
    private final ContractNegotiationService contractNegotiationService;
    private final AgreementFetcherService agreementService;
    private final TransferProcessService transferProcessService;
    private final PublicUrlHandlerService publicUrlHandlerService;
    private final EdcProcessStateService edcProcessStateService;
    private final ChatMessageService chatMessageService;
    private final AssetCreationService assetCreationService;
    private final PolicyCreationService policyCreationService;
    private final ContractDefinitionService contractDefinitionService;
    private final AppConfig appConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void initializePreEdcProcess() {
        if (assetCreationService.checkAssetPresent()) {
            log.info("Asset already exists. Not required to create Asset, Policy and ContractDefinition...");
            return;
        }
        assetCreationService.createAsset();
        policyCreationService.createPolicy();
        contractDefinitionService.createContractDefinition();
    }

    @Async
    public void initProcess(ChatRequest chatMessage) {
        String receiverBpnl = chatMessage.receiverBpn();
        String receiverDspUrl = partnerService.getBusinessPartnerByBpn(receiverBpnl);
        Validate.isFalse(StringUtils.hasText(receiverDspUrl)).launch("Business Partner not registered with BPN: " + receiverBpnl);
        EdcProcessState edcProcessState = edcProcessStateService.getEdcByBpn(receiverBpnl);
        ChatMessage chatResponse = chatMessageService.createChat(chatMessage, true, false);
        if (Objects.nonNull(edcProcessState) && StringUtils.hasText(edcProcessState.getOfferId())) {
            chatMessageService.updateChat(chatResponse, false, edcProcessState);

            String negotiationId = contractNegotiationService.initNegotiation(receiverDspUrl, receiverBpnl, edcProcessState.getOfferId(), edcProcessState);
            if (!StringUtils.hasText(negotiationId)) {
                log.error("Not able to initiate the negotiation for EDC {} and offerId {}, please check manually.", receiverDspUrl, edcProcessState.getOfferId());
                return;
            }
            edcProcessState = setAndSaveEdcState(NEGOTIATION_ID, negotiationId, edcProcessState);

            // Get agreement Id based on the negotiationId
            String agreementId = agreementService.getAgreement(negotiationId, edcProcessState);
            if (!StringUtils.hasText(agreementId)) {
                log.error("Not able to get the agreement for offerId {} and negotiationId {}, please check manually.", edcProcessState.getOfferId(),
                        edcProcessState.getNegotiationId());
                return;
            }
            edcProcessState = setAndSaveEdcState(AGREEMENT_ID, agreementId, edcProcessState);

            //  String agreementId = edcProcessState.getAgreementId();
            String transferProcessId = transferProcessService.initiateTransfer(agreementId, edcProcessState);
            if (!StringUtils.hasText(transferProcessId)) {
                log.error("Not able to get the agreement for transferProcessId {}, please check manually.", transferProcessId);
                return;
            }
            edcProcessState = setAndSaveEdcState("TransferId", transferProcessId, edcProcessState);

            ChatRequest recieverMsg = new ChatRequest(appConfig.bpn(), chatMessage.message());
            publicUrlHandlerService.getAuthCodeAndPublicUrl(transferProcessId, recieverMsg, edcProcessState);
            chatMessageService.updateChat(chatResponse, true, null);
            return;
        }

        if (Objects.isNull(edcProcessState)) {
            edcProcessState = new EdcProcessState();
            edcProcessState.setReceiverBpn(receiverBpnl);
        }

        // Query the catalog for chat asset
        String offerId = queryCatalogService.queryCatalog(receiverDspUrl, receiverBpnl, edcProcessState);
        if (!StringUtils.hasText(offerId)) {
            chatMessageService.updateChat(chatResponse, false, edcProcessState);
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

        ChatRequest receiverMsg = new ChatRequest(appConfig.bpn(), chatMessage.message());
        publicUrlHandlerService.getAuthCodeAndPublicUrl(transferProcessId, receiverMsg, edcProcessState);
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

    public ChatMessage receiveMessage(ChatRequest message) {
        log.info("Received message: {}", message);
        return chatMessageService.createChat(message, false, true);
    }

    @SneakyThrows
    public List<ChatHistoryResponse> getChatHistory(String partnerBpn) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByPartnerBpnOrderByIdAsc(partnerBpn);
        return chatMessages.stream()
                .map(this::mapToChatHistoryResponse)
                .toList();
    }

    private ChatHistoryResponse mapToChatHistoryResponse(ChatMessage message) {
        String sender = findSender(message);
        String receiver = findReceiver(message);
        MessageStatus status = findStatus(message);

        return new ChatHistoryResponse(
                message.getId(),
                receiver,
                sender,
                message.getMessage(),
                status,
                message.getCreatedAt().getTime(),
                message.getEdcProcessState() != null ? message.getEdcProcessState().getErrorDetail() : null);

    }

    private String findSender(ChatMessage message) {
        return message.getSelfOwner() ? appConfig.bpn() : message.getPartnerBpn();
    }

    private String findReceiver(ChatMessage message) {
        return message.getSelfOwner() ? message.getPartnerBpn() : appConfig.bpn();
    }

    private MessageStatus findStatus(ChatMessage message) {
        if (message.getEdcProcessState() != null) {
            if (!StringUtils.hasText(message.getEdcProcessState().getOfferId())) {
                return MessageStatus.QUERY_CATALOG_FAILED; //"QueryCatalogFailed";
            }
            if (!StringUtils.hasText(message.getEdcProcessState().getNegotiationId())) {
                return MessageStatus.NEGOTIATION_FAILED; //"NegotiationFailed";
            }
            if (!StringUtils.hasText(message.getEdcProcessState().getAgreementId())) {
                return MessageStatus.AGREEMENT_FAILED; //"AgreementFailed";
            }
            if (!StringUtils.hasText(message.getEdcProcessState().getTransferId())) {
                return MessageStatus.TRANSFER_PROCESS_FAILED; //"TransferProcessFailed";
            }
            return MessageStatus.SENT; //"sent";
        }
        return MessageStatus.NONE;
    }
}
