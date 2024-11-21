package com.smartsense.chat.web;

import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.utils.request.BusinessPartnerRequest;
import com.smartsense.chat.utils.response.BpnResponse;
import com.smartsense.chat.utils.response.BusinessPartnerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/partners")
public class BusinessPartnerResource {

    private final BusinessPartnerService businessPartnerService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public BusinessPartnerResponse createBusinessPartner(@RequestBody BusinessPartnerRequest request) {
        return businessPartnerService.createBusinessPartner(request);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public BpnResponse getBusinessPartner(@RequestParam(required = false) String name) {
        return StringUtils.hasText(name) ? businessPartnerService.getBusinessPartner(name) : businessPartnerService.getAllBusinessPartners();
    }
}

