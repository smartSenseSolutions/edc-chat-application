/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.EdcOfferDetails;
import com.smartsensesolutions.commons.dao.base.BaseRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface EdcOfferDetailsRepository extends BaseRepository<EdcOfferDetails, Long> {

    EdcOfferDetails findByReceiverBpnAndOfferIdNotNull(String receiverBpn);

    EdcOfferDetails findByReceiverBpn(String receiverBpn);
}
