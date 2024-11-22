package com.smartsense.chat.service;

import com.smartsense.chat.dao.entity.EdcProcessState;
import com.smartsense.chat.dao.repository.EdcProcessStateRepository;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import com.smartsensesolutions.commons.dao.base.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EdcProcessStateService extends BaseService<EdcProcessState, Long> {

    private final EdcProcessStateRepository edcProcessStateRepository;

    public EdcProcessState getEdcByBpn(String receiverBpn) {
        return edcProcessStateRepository.findByReceiverBpn(receiverBpn);
    }

    @Override
    protected BaseRepository<EdcProcessState, Long> getRepository() {
        return edcProcessStateRepository;
    }
}
