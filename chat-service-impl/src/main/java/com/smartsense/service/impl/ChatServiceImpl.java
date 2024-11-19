package com.smartsense.service.impl;

import com.smartsense.api.model.request.ChatRequest;
import com.smartsense.api.model.response.ChatResponse;
import com.smartsense.api.utils.Validate;
import com.smartsense.dao.entity.ChatMaster;
import com.smartsense.dao.repository.ChatMasterRepository;
import com.smartsense.service.ChatService;
import com.smartsense.service.entity.BaseService;
import com.smartsense.service.entity.UserMasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl extends BaseService implements ChatService {

    private final ChatMasterRepository chatMasterRepository;
    private final UserMasterService userMasterService;

    @Override
    public ChatResponse createChat(ChatRequest request) {
        Validate.isTrue(userMasterService.get(List.of(request.senderId(), request.receiverId())).size() != 2).launch("Sender or Receiver is not present");
        ChatMaster chatMaster = ChatMaster.builder()
                .senderId(request.senderId())
                .receiverId(request.receiverId())
                .message(request.message())
                .messageType(request.messageType())
                .build();

        return toType(chatMasterRepository.save(chatMaster), ChatResponse.class);
    }
}
