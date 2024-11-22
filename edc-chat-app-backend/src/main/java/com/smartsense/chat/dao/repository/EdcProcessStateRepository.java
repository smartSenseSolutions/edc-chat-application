package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.EdcProcessState;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdcProcessStateRepository extends BaseRepository<EdcProcessState, Long> {

    EdcProcessState findByReceiverBpn(String receiverBpn);
    
}
