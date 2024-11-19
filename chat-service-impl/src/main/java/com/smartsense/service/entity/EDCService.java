package com.smartsense.service.entity;

import com.smartsense.api.constant.ContMessage;
import com.smartsense.api.model.request.EDCRegisterRequest;
import com.smartsense.api.model.response.UsersEDCResponse;
import com.smartsense.api.utils.Validate;
import com.smartsense.dao.entity.EDCRegisterMaster;
import com.smartsense.dao.entity.UserMaster;
import com.smartsense.dao.repository.EDCRegisterMasterRepository;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import com.smartsensesolutions.commons.dao.specification.SpecificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EDCService extends BaseEntityService<EDCRegisterMaster, UUID> {

    @Autowired
    private EDCRegisterMasterRepository edcRegisterMasterRepository;
    @Autowired
    private SpecificationUtil<EDCRegisterMaster> specificationUtil;

    public EDCRegisterMaster createEDC(EDCRegisterRequest request, UserMaster user) {
        Validate.isTrue(edcRegisterMasterRepository.existsByEdcUrl(request.edcUrl())).launch(ContMessage.VALIDATE_EDC_CREATE_URL_EXIST);
        EDCRegisterMaster edcRegisterMaster = EDCRegisterMaster.builder()
                .edcUrl(request.edcUrl())
                .userId(user)
                .build();
        return create(edcRegisterMaster);
    }


    @Override
    protected BaseRepository<EDCRegisterMaster, UUID> getRepository() {
        return edcRegisterMasterRepository;
    }

    public UsersEDCResponse getEDCByUserid(UUID userId) {
        List<EDCRegisterMaster> edcList = edcRegisterMasterRepository.findByUserId(userId);
        List<String> edcUrls = edcList.stream().map(EDCRegisterMaster::getEdcUrl).toList();
        return new UsersEDCResponse(userId, edcUrls);
    }
}
