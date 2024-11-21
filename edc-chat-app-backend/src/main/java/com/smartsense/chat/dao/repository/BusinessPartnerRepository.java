package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.BusinessPartner;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessPartnerRepository extends BaseRepository<BusinessPartner, UUID> {

    BusinessPartner findByName(String name);

    BusinessPartner findByBpn(String bpn);

    @Query("select b from BusinessPartner b where b.name = ?1 or b.bpn = ?2")
    BusinessPartner findByNameOrBpn(String name, String bpn);
}
