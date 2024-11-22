package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage, Long> {
}
