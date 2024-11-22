package com.smartsense.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsense.chat.dao.entity.BusinessPartner;
import com.smartsense.chat.dao.repository.BusinessPartnerRepository;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.utils.exception.ConflictException;
import com.smartsense.chat.utils.request.BusinessPartnerRequest;
import com.smartsense.chat.utils.response.BpnResponse;
import com.smartsense.chat.utils.response.BusinessPartnerResponse;
import com.smartsense.chat.utils.validate.Validate;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import com.smartsensesolutions.commons.dao.base.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessPartnerService extends BaseService<BusinessPartner, UUID> {

    private final BusinessPartnerRepository businessPartnerRepository;
    private final ObjectMapper mapper;
    private final AppConfig config;


    public BusinessPartnerResponse createBusinessPartner(BusinessPartnerRequest request) {
        log.info("Business partner request: {}", request);
        Validate.isTrue(Objects.equals(config.bpn(), request.bpn())).launch("This BPN is configured. Not a valid BPN: " + request.bpn());
        BusinessPartner partner = businessPartnerRepository.findByBpn(request.bpn());
        if (Objects.nonNull(partner)) {
            String errorMessage = String.format("BusinessPartner with BPN '%s' already exists.", request.bpn());
            log.error(errorMessage);
            throw new ConflictException(errorMessage);
        }
        log.info("Creating BusinessPartner. name: {}", request.name());
        BusinessPartner businessPartner = BusinessPartner.builder()
                .name(request.name())
                .edcUrl(request.edcUrl())
                .bpn(request.bpn())
                .build();
        return mapper.convertValue(businessPartnerRepository.save(businessPartner), BusinessPartnerResponse.class);
    }

    public List<BpnResponse> getBusinessPartner(String name) {
        List<BpnResponse> response = new ArrayList<>();
        List<BusinessPartner> businessPartner = businessPartnerRepository.findByName(name);
        Validate.isTrue(CollectionUtils.isEmpty(businessPartner)).launch("No Business partner found with name: " + name);
        for (BusinessPartner partner : businessPartner) {
            response.add(new BpnResponse(partner.getBpn(), partner.getName()));
        }
        return response;
    }

    public List<BpnResponse> getAllBusinessPartners() {
        List<BpnResponse> response = new ArrayList<>();
        List<BusinessPartner> businessPartnerList = businessPartnerRepository.findAll();
        for (BusinessPartner partner : businessPartnerList) {
            response.add(new BpnResponse(partner.getBpn(), partner.getName()));
        }
        return response;
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


    @SneakyThrows
    public List<Map> getChatHistory(){
        String history = """
                [
                    {
                        "receiver": "BPNL000000000001",
                        "sender": "BPNL000000000TATA",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000TATA",
                        "sender": "BPNL000000000001",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000001",
                        "sender": "BPNL000000000TATA",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000TATA",
                        "sender": "BPNL000000000001",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000001",
                        "sender": "BPNL000000000TATA",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    },
                    {
                        "receiver": "BPNL000000000TATA",
                        "sender": "BPNL000000000001",
                        "content": "Hello! How can I help you?",
                        "timestamp": 1700654400,
                        "status": "sent"
                    }
                ]
                """;
        return mapper.readValue(history, List.class);
    }
}
