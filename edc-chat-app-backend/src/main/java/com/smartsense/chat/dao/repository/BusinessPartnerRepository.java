package com.smartsense.chat.dao.repository;

import com.smartsense.chat.dao.entity.BusinessPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessPartnerRepository extends JpaRepository<BusinessPartner, UUID> {

    BusinessPartner findByBpn(String bpn);
}
