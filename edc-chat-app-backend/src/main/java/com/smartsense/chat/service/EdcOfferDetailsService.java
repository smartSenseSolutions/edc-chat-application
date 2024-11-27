package com.smartsense.chat.service;

import com.smartsense.chat.dao.entity.EdcOfferDetails;
import com.smartsense.chat.dao.repository.EdcOfferDetailsRepository;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import com.smartsensesolutions.commons.dao.base.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EdcOfferDetailsService extends BaseService<EdcOfferDetails, Long> {

    private final EdcOfferDetailsRepository edcOfferDetailsRepository;

    @Override
    protected BaseRepository<EdcOfferDetails, Long> getRepository() {
        return edcOfferDetailsRepository;
    }

    public EdcOfferDetails getOfferDetails(String receiverBpnl) {
        return edcOfferDetailsRepository.findByReceiverBpnAndOfferIdNotNull(receiverBpnl);
    }
}
