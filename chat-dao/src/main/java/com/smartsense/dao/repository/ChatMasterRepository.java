package com.smartsense.dao.repository;

import com.smartsense.dao.entity.ChatMaster;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMasterRepository extends BaseRepository<ChatMaster, UUID> {

}
