package com.smartsense.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsense.chat.dao.entity.BusinessPartner;
import com.smartsense.chat.dao.repository.BusinessPartnerRepository;
import com.smartsense.chat.utils.request.BusinessPartnerRequest;
import com.smartsense.chat.utils.response.BpnResponse;
import com.smartsense.chat.utils.response.BusinessPartnerResponse;
import com.smartsense.chat.utils.validate.Validate;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import com.smartsensesolutions.commons.dao.base.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessPartnerService extends BaseService<BusinessPartner, UUID> {

    private final BusinessPartnerRepository businessPartnerRepository;
    private final ObjectMapper mapper;


    public BusinessPartnerResponse createBusinessPartner(BusinessPartnerRequest request) {
        BusinessPartner partner = businessPartnerRepository.findByNameOrBpn(request.name(), request.bpn());
        if (Objects.nonNull(partner)) {
            log.info("Updating BusinessPartner for bpn: {}", request.bpn());
            partner.setName(request.name());
            partner.setEdcUrl(request.edcUrl());
            partner.setBpn(request.bpn());
        } else {
            log.info("Creating BusinessPartner. name: {}", request.name());
            partner = BusinessPartner.builder()
                    .name(request.name())
                    .edcUrl(request.edcUrl())
                    .bpn(request.bpn())
                    .build();
        }
        return mapper.convertValue(businessPartnerRepository.save(partner), BusinessPartnerResponse.class);
    }

    public BpnResponse getBusinessPartner(String name) {
        BusinessPartner businessPartner = businessPartnerRepository.findByName(name);
        Validate.isTrue(Objects.isNull(businessPartner)).launch("No Business partner found with name: " + name);
        return new BpnResponse(Map.of(businessPartner.getName(), businessPartner.getBpn()));
    }

    public BpnResponse getAllBusinessPartners() {
        List<BusinessPartner> businessPartnerList = businessPartnerRepository.findAll();
        Map<String, String> bpnMap = businessPartnerList.stream().collect(Collectors.toMap(BusinessPartner::getName, BusinessPartner::getBpn, (k, v) -> v));
        return new BpnResponse(bpnMap);
    }

    @Override
    protected BaseRepository<BusinessPartner, UUID> getRepository() {
        return businessPartnerRepository;
    }

    public String getBusinessPartnerByBpn(String bpn) {
        BusinessPartner partner = businessPartnerRepository.findByBpn(bpn);
        Validate.isTrue(Objects.isNull(partner)).launch("No Business partner found with bpn: " + bpn);
        return partner.getEdcUrl();
    }
}
