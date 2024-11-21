package com.smartsense.chat.service;

import com.smartsense.chat.dao.entity.BusinessPartner;
import com.smartsense.chat.dao.repository.BusinessPartnerRepository;
import com.smartsense.chat.utils.request.BusinessPartnerRequest;
import com.smartsense.chat.web.BusinessPartnerResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessPartnerService {

    private final BusinessPartnerRepository businessPartnerRepository;


    public BusinessPartnerResource createBusinessPartner(BusinessPartnerRequest request) {
        log.info("Creating BusinessPartner. name: {}", request.name());
        BusinessPartner businessPartner = BusinessPartner.builder()
                .name(request.name())
                .edcUrl(request.edcUrl())
                .bpn(request.bpn())
                .build();
        businessPartnerRepository.save(businessPartner);
        return null;
    }
}
