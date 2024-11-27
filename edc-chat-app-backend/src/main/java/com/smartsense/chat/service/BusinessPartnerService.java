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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessPartnerService extends BaseService<BusinessPartner, UUID> {

    private final BusinessPartnerRepository businessPartnerRepository;
    private final ObjectMapper mapper;
    private final AppConfig config;


    /**
     * Creates a new business partner based on the provided request.
     * <p>
     * This method validates the BPN, checks for existing partners with the same BPN,
     * and creates a new BusinessPartner entity if validation passes.
     *
     * @param request The BusinessPartnerRequest containing details for creating a new business partner.
     *                It should include name, EDC URL, and BPN (Business Partner Number).
     * @return A BusinessPartnerResponse object containing the details of the newly created business partner.
     *
     * @throws ConflictException If a business partner with the provided BPN already exists.
     */
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


    /**
     * Retrieves a list of all business partners in the system.
     * <p>
     * This method fetches all business partners from the repository and
     * transforms them into a list of BpnResponse objects, containing
     * only the BPN (Business Partner Number) and name of each partner.
     *
     * @return A List of BpnResponse objects, each containing the BPN and name
     * of a business partner. If no business partners are found,
     * an empty list is returned.
     */
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


    /**
     * Retrieves the EDC URL of a business partner based on their BPN (Business Partner Number).
     *
     * @param bpn The Business Partner Number (BPN) used to identify the business partner.
     * @return The EDC URL associated with the business partner.
     *
     * @throws com.smartsense.chat.utils.exception.BadDataException If no business partner is found with the given BPN.
     */
    public String getBusinessPartnerByBpn(String bpn) {
        BusinessPartner partner = businessPartnerRepository.findByBpn(bpn);
        Validate.isTrue(Objects.isNull(partner)).launch("No Business partner found with bpn: " + bpn);
        return partner.getEdcUrl();
    }
}
