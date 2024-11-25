package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage, Long> {

    @Query("select c from ChatMessage c where c.partnerBpn = ?1 and c.chatSuccess = true")
    List<ChatMessage> findByPartnerBpnAndChatSuccessTrue(String partnerBpn);
    
}
