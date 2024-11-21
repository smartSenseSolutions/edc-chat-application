package com.smartsense.chat.web;

import com.smartsense.chat.dao.entity.BusinessPartner;
import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.utils.request.BusinessPartnerRequest;
import com.smartsense.chat.utils.response.BpnResponse;
import com.smartsense.chat.utils.response.BusinessPartnerResponse;
import com.smartsensesolutions.commons.dao.filter.FilterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/partner")
public class BusinessPartnerResource {

    private final BusinessPartnerService businessPartnerService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public BusinessPartnerResponse createBusinessPartner(@RequestBody BusinessPartnerRequest request) {
        return businessPartnerService.createBusinessPartner(request);
    }

    @GetMapping(value = "/get", produces = APPLICATION_JSON_VALUE)
    public BpnResponse getBusinessPartner(@RequestParam(required = false) String name) {
        return StringUtils.hasText(name) ? businessPartnerService.getBusinessPartner(name) : businessPartnerService.getAllBusinessPartners();
    }

    @PostMapping(value = "/fetch", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Page<BusinessPartner> fetchRawData(@RequestBody FilterRequest request) {
        return businessPartnerService.filter(request);
    }
}

