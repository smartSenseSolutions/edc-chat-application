package com.smartsense.chat.web;

import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.BusinessPartnerService;
import com.smartsense.chat.utils.request.BusinessPartnerRequest;
import com.smartsense.chat.utils.response.BpnResponse;
import com.smartsense.chat.utils.response.BusinessPartnerResponse;
import com.smartsense.chat.web.apidocs.BusinessPartnersApiDocs;
import com.smartsense.chat.web.apidocs.BusinessPartnersApiDocs.CreateBusinessPartner;
import com.smartsense.chat.web.apidocs.BusinessPartnersApiDocs.GetBusinessPartners;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import static com.smartsense.chat.web.ApiConstant.CONFIG;
import static com.smartsense.chat.web.ApiConstant.PARTNERS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Business Partner management Controller", description = "This controller used for persist Business Partner details.")
public class BusinessPartnerResource {

    private final BusinessPartnerService businessPartnerService;
    private final AppConfig config;


    /**
     * Retrieves the current application configuration.
     * <p>
     * This method is used to fetch the current configuration settings of the application.
     * It returns an AppConfig object that contains various configuration parameters.
     *
     * @return AppConfig An object containing the current application configuration settings.
     */
    @BusinessPartnersApiDocs.GetConfiguration
    @GetMapping(value = CONFIG, produces = APPLICATION_JSON_VALUE)
    public AppConfig getConfig() {
        return config;
    }

    /**
     * Creates a new business partner based on the provided request.
     * <p>
     * This method handles the creation of a new business partner by processing
     * the information provided in the BusinessPartnerRequest. It delegates the
     * actual creation to the businessPartnerService.
     *
     * @param request The BusinessPartnerRequest object containing the details
     *                of the business partner to be created. This should include
     *                all necessary information for partner creation.
     * @return BusinessPartnerResponse An object containing the details of the
     * newly created business partner, including any system-generated
     * fields or IDs.
     */
    @CreateBusinessPartner
    @PostMapping(value = PARTNERS, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public BusinessPartnerResponse createBusinessPartner(@RequestBody BusinessPartnerRequest request) {
        return businessPartnerService.createBusinessPartner(request);
    }

    
    /**
     * Retrieves a list of all business partners.
     * <p>
     * This method fetches all business partners stored in the system and returns them
     * as a list of BpnResponse objects. It uses the businessPartnerService to perform
     * the actual retrieval operation.
     *
     * @return List<BpnResponse> A list of BpnResponse objects, each representing a
     * business partner. The list may be empty if no business partners are found.
     */
    @GetBusinessPartners
    @GetMapping(value = PARTNERS, produces = APPLICATION_JSON_VALUE)
    public List<BpnResponse> getBusinessPartner() {
        return businessPartnerService.getAllBusinessPartners();
    }
}
