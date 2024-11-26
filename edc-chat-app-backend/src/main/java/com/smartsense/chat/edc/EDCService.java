package com.smartsense.chat.edc;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.dao.entity.EdcOfferDetails;
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
import com.smartsense.chat.service.EdcOfferDetailsService;
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
    private final EdcOfferDetailsService edcOfferDetailsService;
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
    public void initProcess(ChatRequest chatRequest) {
        String receiverBpnl = chatRequest.receiverBpn();
        String receiverDspUrl = partnerService.getBusinessPartnerByBpn(receiverBpnl);
        Validate.isFalse(StringUtils.hasText(receiverDspUrl)).launch("Business Partner not registered with BPN: " + receiverBpnl);
        ChatMessage chatMessage = chatMessageService.createChat(chatRequest, true, false, null);

        EdcOfferDetails edcOfferDetails = edcOfferDetailsService.getOfferDetails(receiverBpnl);
        if (Objects.isNull(edcOfferDetails)) {
            edcOfferDetails = new EdcOfferDetails();
            edcOfferDetails.setReceiverBpn(receiverBpnl);
            edcOfferDetails.setAssetId(appConfig.edc().assetId());
            edcOfferDetails = edcOfferDetailsService.create(edcOfferDetails);

            chatMessageService.updateChat(chatMessage, edcOfferDetails, false);
            // Query the catalog for chat asset
            String offerId = queryCatalogService.queryCatalog(receiverDspUrl, receiverBpnl, chatMessage);
            if (!StringUtils.hasText(offerId)) {
                log.error("Not able to create and retrieve the offerId from EDC {}, please check manually.", receiverDspUrl);
                Validate.isTrue(true).launch(String.format("Not able to create and retrieve the offerId from EDC %s ", receiverDspUrl));
                return;
            }
            edcOfferDetails.setOfferId(offerId);
            edcOfferDetails = edcOfferDetailsService.create(edcOfferDetails);
        } else {
            chatMessageService.updateChat(chatMessage, edcOfferDetails, false);
        }

        // Initiate the contract negotiation
        String offerId = edcOfferDetails.getOfferId();
        String negotiationId = contractNegotiationService.initNegotiation(receiverDspUrl, receiverBpnl, offerId, chatMessage);
        if (!StringUtils.hasText(negotiationId)) {
            log.error("Not able to initiate the negotiation for EDC {} and offerId {}, please check manually.", receiverDspUrl, offerId);
            return;
        }
        chatMessageService.setAndSaveEdcState("NegotiationId", negotiationId, chatMessage);

        // Get agreement Id based on the negotiationId
        String agreementId = agreementService.getAgreement(negotiationId, chatMessage);
        if (!StringUtils.hasText(agreementId)) {
            log.error("Not able to get the agreement for offerId {} and negotiationId {}, please check manually.", offerId, negotiationId);
            return;
        }
        chatMessageService.setAndSaveEdcState("AgreementId", agreementId, chatMessage);

        // Initiate the transfer process
        String transferProcessId = transferProcessService.initiateTransfer(agreementId, chatMessage);
        if (!StringUtils.hasText(transferProcessId)) {
            log.error("Not able to get the agreement for transferProcessId {}, please check manually.", transferProcessId);
            return;
        }
        chatMessageService.setAndSaveEdcState("TransferId", transferProcessId, chatMessage);

        // Sent the message to public url
        ChatRequest receiverMsg = new ChatRequest(appConfig.bpn(), chatRequest.message());
        publicUrlHandlerService.getAuthCodeAndPublicUrl(transferProcessId, receiverMsg, chatMessage);
        if (!StringUtils.hasText(chatMessage.getErrorDetail())) {
            chatMessageService.updateChat(chatMessage, edcOfferDetails, true);
        }
    }

    public ChatMessage receiveMessage(ChatRequest message) {
        log.info("Received message: {}", message);
        return chatMessageService.createChat(message, false, true, null);
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
                message.getErrorDetail());
    }

    private String findSender(ChatMessage message) {
        return message.getSelfOwner() ? appConfig.bpn() : message.getPartnerBpn();
    }

    private String findReceiver(ChatMessage message) {
        return message.getSelfOwner() ? message.getPartnerBpn() : appConfig.bpn();
    }

    private MessageStatus findStatus(ChatMessage message) {
        if (Objects.nonNull(message.getEdcOfferDetails()) && !StringUtils.hasText(message.getEdcOfferDetails().getOfferId())) {
            return MessageStatus.QUERY_CATALOG_FAILED;
        }
        if (!StringUtils.hasText(message.getNegotiationId())) {
            return MessageStatus.NEGOTIATION_FAILED;
        }
        if (!StringUtils.hasText(message.getAgreementId())) {
            return MessageStatus.AGREEMENT_FAILED;
        }
        if (!StringUtils.hasText(message.getTransferId())) {
            return MessageStatus.TRANSFER_PROCESS_FAILED;
        }
        if (StringUtils.hasText(message.getErrorDetail())) {
            return MessageStatus.CALL_PUBLIC_URL_PROCESS_FAILED;
        }
        return MessageStatus.SENT;
    }
}
