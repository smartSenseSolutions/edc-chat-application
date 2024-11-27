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

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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


    /**
     * Initializes the EDC (Eclipse Dataspace Connector) process which are needed to start chat(data transfer).
     * This method is triggered when the application is ready, as indicated by the ApplicationReadyEvent.
     * It checks if an asset already exists in EDC, and if not, it creates the necessary asset, policy, and contract definition.
     * <p>
     * It uses the following services:
     * - assetCreationService: to check for existing assets and create new ones if needed
     * - policyCreationService: to create a new policy
     * - contractDefinitionService: to create a new contract definition
     */
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


    /**
     * Initializes the process for sending a chat message through the Eclipse Dataspace Connector (EDC).
     * This method handles the entire flow from creating a chat message to negotiating a contract,
     * obtaining an agreement, initiating a transfer process, and finally sending the message to a public URL of counterparty.
     * <p>
     * The process includes:
     * 1. Validating the receiver's business partner number (BPN)
     * 2. Query Catalog
     * 3. Creating or retrieving EDC offer details
     * 4. Initiating contract negotiation
     * 5. Obtaining an agreement
     * 6. Initiating the transfer process
     * 7. Sending the message to a public URL of receiver
     * <p>
     * This method is executed asynchronously.
     *
     * @param chatRequest The chat request containing information about the message to be sent,
     *                    including the receiver's BPN and the message content.
     */
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


    /**
     * Receives and processes an incoming chat message from sender.
     * This method logs the received message and creates a new chat entry in the system.
     *
     * @param message The ChatRequest object containing the incoming message details.
     * @return ChatMessage A new ChatMessage object representing the processed and stored chat message.
     */
    public ChatMessage receiveMessage(ChatRequest message) {
        log.info("Received message: {}", message);
        return chatMessageService.createChat(message, false, true, null);
    }


    /**
     * Retrieves the chat history for a specific business partner.
     * This method fetches all chat messages associated with the given partner BPN,
     * orders them by ID in ascending order, and maps them to ChatHistoryResponse objects.
     *
     * @param partnerBpn The Business Partner Number (BPN) of the partner for whom to retrieve the chat history.
     * @return A List of ChatHistoryResponse objects representing the chat history with the specified partner,
     * ordered by message ID in ascending order.
     */
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
        MessageStatus status = Boolean.FALSE.equals(message.getSelfOwner()) ? MessageStatus.RECEIVED : MessageStatus.SENT;

        if (Boolean.TRUE.equals(message.getSelfOwner()) && !message.isChatSuccess()) {
            status = findStatus(message);
        }

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
