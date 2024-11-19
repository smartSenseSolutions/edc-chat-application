package com.smartsense.web.resource;

import com.smartsense.api.constant.ContField;
import com.smartsense.api.constant.ContMessage;
import com.smartsense.api.constant.ContURI;
import com.smartsense.api.model.request.EDCRegisterRequest;
import com.smartsense.api.model.request.EDCUpdateRequest;
import com.smartsense.api.model.response.EDCRegisterResponse;
import com.smartsense.api.model.response.PageResponse;
import com.smartsense.api.model.response.ResponseBody;
import com.smartsense.api.model.response.UsersEDCResponse;
import com.smartsense.service.EDCRegisterService;
import com.smartsense.web.apidocs.EDCRegisterResourceApiDocs.GetEDCById;
import com.smartsense.web.apidocs.EDCRegisterResourceApiDocs.GetEDCByUserId;
import com.smartsense.web.apidocs.EDCRegisterResourceApiDocs.RegisterEDC;
import com.smartsense.web.apidocs.EDCRegisterResourceApiDocs.UpdateEDC;
import com.smartsensesolutions.commons.dao.filter.FilterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@Slf4j
public class EDCRegisterResource extends BaseResource {

    private final EDCRegisterService edcRegisterService;


    @RegisterEDC
    @Operation(summary = "Create EDC")
    @PostMapping(value = ContURI.EDC_CREATE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<EDCRegisterResponse> createEDC(@RequestBody EDCRegisterRequest request) {
        EDCRegisterResponse edc = edcRegisterService.createEDCRegister(request);
        return ResponseBody.of(resolveMessage(ContMessage.EDC_CREATED), edc);
    }

    @UpdateEDC
    @Operation(summary = "Update EDC")
    @PutMapping(value = ContURI.EDC_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<EDCRegisterResponse> updateEDC(@RequestBody EDCUpdateRequest request) {
        EDCRegisterResponse edc = edcRegisterService.updateEDCRegister(request);
        return ResponseBody.of(resolveMessage(ContMessage.EDC_CREATED), edc);
    }

    @GetEDCById
    @Operation(summary = "Get EDC")
    @GetMapping(value = ContURI.GET_EDC, produces = MediaType.APPLICATION_JSON_VALUE)
    public EDCRegisterResponse getUser(@PathVariable("edcId") UUID edcId) {
        return edcRegisterService.getEDCById(edcId);
    }

    @GetEDCByUserId
    @Operation(summary = "Get User's all EDC")
    @GetMapping(value = ContURI.GET_EDC_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public UsersEDCResponse getEDCByUserId(@PathVariable(ContField.USER_ID) UUID userId) {
        return edcRegisterService.getEDCUserById(userId);
    }

    @Operation(summary = "EDC Register Filter")
    @PostMapping(value = ContURI.EDC_FILTER, produces = MediaType.APPLICATION_JSON_VALUE, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<EDCRegisterResponse> userFilter(@Valid @RequestBody FilterRequest filterRequest) {
        return edcRegisterService.filter(filterRequest);
    }
}
