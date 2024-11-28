/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsensesolutions.commons.dao.base.BaseRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage, Long> {

    List<ChatMessage> findByPartnerBpnOrderByIdAsc(String partnerBpn);

    ChatMessage findByPartnerBpn(String partnerBpn);
}
