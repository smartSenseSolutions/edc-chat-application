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

import static com.smartsense.chat.edc.constant.EdcConstant.AGREEMENT_ID;
import static com.smartsense.chat.edc.constant.EdcConstant.NEGOTIATION_ID;
import static com.smartsense.chat.edc.constant.EdcConstant.TRANSFER_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService extends BaseService<ChatMessage, Long> {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    protected BaseRepository<ChatMessage, Long> getRepository() {
        return chatMessageRepository;
    }

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

    @Async
    public void updateChat(ChatMessage chatMessage, EdcOfferDetails edcOfferDetails, boolean success) {
        chatMessage.setChatSuccess(success);
        chatMessage.setEdcOfferDetails(edcOfferDetails);
        create(chatMessage);
    }

    @Async
    public void setAndSaveEdcState(String fieldName, String value, ChatMessage chatMessage) {
        switch (fieldName) {
            case NEGOTIATION_ID -> chatMessage.setNegotiationId(value);
            case AGREEMENT_ID -> chatMessage.setAgreementId(value);
            case TRANSFER_ID -> chatMessage.setTransferId(value);
            default -> throw new IllegalArgumentException("Unsupported field name: " + fieldName);
        }
        create(chatMessage);
    }
}
