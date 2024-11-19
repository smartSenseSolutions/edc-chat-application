package com.smartsense.service;

import com.smartsense.api.model.request.EDCRegisterRequest;
import com.smartsense.api.model.request.EDCUpdateRequest;
import com.smartsense.api.model.response.EDCRegisterResponse;
import com.smartsense.api.model.response.PageResponse;
import com.smartsense.api.model.response.UsersEDCResponse;
import com.smartsensesolutions.commons.dao.filter.FilterRequest;

import java.util.UUID;

public interface EDCRegisterService {

    EDCRegisterResponse createEDCRegister(EDCRegisterRequest request);

    EDCRegisterResponse updateEDCRegister(EDCUpdateRequest request);

    EDCRegisterResponse getEDCById(UUID id);

    PageResponse<EDCRegisterResponse> filter(FilterRequest filterRequest);

    UsersEDCResponse getEDCUserById(UUID userId);
}
