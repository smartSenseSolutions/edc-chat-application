package com.smartsense.chat.web;

import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.utils.request.BusinessPartnerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/partner")
public class BusinessPartnerResource {

    private final BusinessPartnerService businessPartnerService;


    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public BusinessPartnerResource createBusinessPartner(@RequestBody BusinessPartnerRequest request) {
        return businessPartnerService.createBusinessPartner(request);
    }
}
