package com.smartsense.chat.service;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.dao.entity.EdcProcessState;
import com.smartsense.chat.dao.repository.ChatMessageRepository;
import com.smartsense.chat.utils.request.ChatRequest;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import com.smartsensesolutions.commons.dao.base.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService extends BaseService<ChatMessage, Long> {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    protected BaseRepository<ChatMessage, Long> getRepository() {
        return chatMessageRepository;
    }

    public ChatMessage createChat(ChatRequest chatMessage, boolean isOwner, boolean success) {
        ChatMessage chat = ChatMessage.builder()
                .message(chatMessage.message())
                .partnerBpn(chatMessage.receiverBpn())
                .selfOwner(isOwner)
                .chatSuccess(success)
                .edcProcessState(null)
                .build();
        return create(chat);
    }

    public void updateChat(ChatMessage chatMessage, boolean isChatSuccess, EdcProcessState edcState) {
        chatMessage.setChatSuccess(isChatSuccess);
        if (Objects.nonNull(edcState)) {
            chatMessage.setEdcProcessState(edcState);
        }
        create(chatMessage);
    }
}
