/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.BusinessPartner;
import com.smartsensesolutions.commons.dao.base.BaseRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessPartnerRepository extends BaseRepository<BusinessPartner, UUID> {

    List<BusinessPartner> findByName(String name);

    BusinessPartner findByBpn(String bpn);

    @Query("select b from BusinessPartner b where b.name = ?1 or b.bpn = ?2")
    BusinessPartner findByNameOrBpn(String name, String bpn);
}
