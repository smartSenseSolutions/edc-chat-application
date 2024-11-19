package com.smartsense.service.impl;

import com.smartsense.api.model.request.EDCRegisterRequest;
import com.smartsense.api.model.request.EDCUpdateRequest;
import com.smartsense.api.model.response.EDCRegisterResponse;
import com.smartsense.api.model.response.PageResponse;
import com.smartsense.api.model.response.UsersEDCResponse;
import com.smartsense.api.utils.Validate;
import com.smartsense.dao.entity.EDCRegisterMaster;
import com.smartsense.dao.entity.UserMaster;
import com.smartsense.service.EDCRegisterService;
import com.smartsense.service.entity.BaseService;
import com.smartsense.service.entity.EDCService;
import com.smartsense.service.entity.UserMasterService;
import com.smartsensesolutions.commons.dao.filter.FilterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EDCRegisterServiceImpl extends BaseService implements EDCRegisterService {

    private final EDCService edcService;
    private final UserMasterService userMasterService;

    @Override
    public EDCRegisterResponse createEDCRegister(EDCRegisterRequest request) {
        UserMaster userMaster = userMasterService.getUser(request.userId());
        return toType(edcService.createEDC(request, userMaster), EDCRegisterResponse.class);
    }

    @Override
    public EDCRegisterResponse updateEDCRegister(EDCUpdateRequest request) {
        UserMaster user = userMasterService.getUser(request.request().userId());
        Validate.isTrue(Objects.isNull(getEDCById(request.edcId()))).launch("EDC not register for given edcId: " + request.edcId());
        return toType(edcService.createEDC(request.request(), user), EDCRegisterResponse.class);
    }

    @Override
    public EDCRegisterResponse getEDCById(UUID edcId) {
        EDCRegisterMaster edcDetails = edcService.get(edcId);
        Validate.isTrue(Objects.isNull(edcDetails)).launch("No EDC registered with EDC Id: " + edcId);
        return toType(edcDetails, EDCRegisterResponse.class);
    }

    @Override
    public PageResponse<EDCRegisterResponse> filter(FilterRequest filterRequest) {
        Page<EDCRegisterMaster> edcFilter = edcService.filter(filterRequest);
        return toPageResponse(edcFilter, filterRequest, EDCRegisterResponse.class);
    }

    @Override
    public UsersEDCResponse getEDCUserById(UUID userId) {
        return edcService.getEDCByUserid(userId);
    }
}
