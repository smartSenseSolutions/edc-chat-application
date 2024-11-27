package com.smartsense.chat.service;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.dao.entity.EdcOfferDetails;
import com.smartsense.chat.dao.repository.ChatMessageRepository;
import com.smartsense.chat.utils.request.ChatRequest;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import com.smartsensesolutions.commons.dao.base.BaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService extends BaseService<ChatMessage, Long> {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    protected BaseRepository<ChatMessage, Long> getRepository() {
        return chatMessageRepository;
    }


    /**
     * Creates a new chat message based on the provided parameters.
     *
     * @param chatMessage     The ChatRequest object containing the message details.
     * @param isOwner         A boolean indicating if the current user is the owner of the chat.
     * @param success         A boolean indicating if the chat creation was successful.
     * @param edcOfferDetails The EdcOfferDetails associated with this chat message.
     * @return A new ChatMessage object created with the provided details.
     */
    public ChatMessage createChat(ChatRequest chatMessage, boolean isOwner, boolean success, EdcOfferDetails edcOfferDetails) {
        ChatMessage chat = ChatMessage.builder()
                .message(chatMessage.message())
                .partnerBpn(chatMessage.receiverBpn())
                .selfOwner(isOwner)
                .chatSuccess(success)
                .edcOfferDetails(edcOfferDetails)
                .build();
        return create(chat);
    }


    /**
     * Asynchronously updates the chat message with new EDC offer details and success status.
     *
     * @param chatMessage     The ChatMessage object to be updated.
     * @param edcOfferDetails The new EdcOfferDetails to be associated with the chat message.
     * @param success         A boolean indicating whether the chat operation was successful.
     */
    @Async
    public void updateChat(ChatMessage chatMessage, EdcOfferDetails edcOfferDetails, boolean success) {
        chatMessage.setChatSuccess(success);
        chatMessage.setEdcOfferDetails(edcOfferDetails);
        create(chatMessage);
    }
    

    /**
     * Asynchronously sets a specific EDC state field on a ChatMessage and saves the updated message.
     * This method updates one of three possible fields (NegotiationId, AgreementId, or TransferId)
     * based on the provided fieldName, and then persists the changes.
     *
     * @param fieldName   The name of the field to be updated. Must be one of "NegotiationId", "AgreementId", or "TransferId".
     * @param value       The value to be set for the specified field.
     * @param chatMessage The ChatMessage object to be updated and saved.
     * @throws IllegalArgumentException If an unsupported fieldName is provided.
     */
    @Async
    public void setAndSaveEdcState(String fieldName, String value, ChatMessage chatMessage) {
        switch (fieldName) {
            case "NegotiationId" -> chatMessage.setNegotiationId(value);
            case "AgreementId" -> chatMessage.setAgreementId(value);
            case "TransferId" -> chatMessage.setTransferId(value);
            default -> throw new IllegalArgumentException("Unsupported field name: " + fieldName);
        }
        create(chatMessage);
    }
}
