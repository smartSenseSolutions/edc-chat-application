package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage, Long> {

    List<ChatMessage> findByPartnerBpnOrderByIdAsc(String partnerBpn);

    ChatMessage findByPartnerBpn(String partnerBpn);
}
