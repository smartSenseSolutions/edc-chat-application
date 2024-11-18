/*
 * Copyright 2024 smartSense Consulting Solutions Pvt. Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartsense.web.resource;

import com.smartsensesolutions.commons.dao.filter.FilterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.smartsense.api.constant.ContField;
import com.smartsense.api.constant.ContMessage;
import com.smartsense.api.constant.ContURI;
import com.smartsense.api.model.request.UserRequest;
import com.smartsense.api.model.response.PageResponse;
import com.smartsense.api.model.response.ResponseBody;
import com.smartsense.api.model.response.UserResponse;
import com.smartsense.service.UserManagementService;
import com.smartsense.web.apidocs.UserManagementResourceApiDocs.Common500;
import com.smartsense.web.apidocs.UserManagementResourceApiDocs.CreateUserApiDocs;

/**
 * Provide endpoint related to User management
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@AllArgsConstructor
@RestController
@Slf4j
@Common500
public class UserManagementResource extends BaseResource {
    private final UserManagementService userManagementService;

    @CreateUserApiDocs
    @PostMapping(value = ContURI.USER, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBody<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse user = userManagementService.createUser(userRequest);
        return ResponseBody.of(resolveMessage(ContMessage.USER_CREATED), user);
    }

    @Operation(summary = "Get User")
    @GetMapping(value = ContURI.USER_WITH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getUser(@PathVariable(ContField.USER_ID) String userId) {
        return userManagementService.getUserById(userId);
    }

    @Operation(summary = "User Filter")
    @PostMapping(value = ContURI.USER_FILTER, produces = MediaType.APPLICATION_JSON_VALUE, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<UserResponse> userFilter(@Valid @RequestBody FilterRequest filterRequest) {
        return userManagementService.userFilter(filterRequest);
    }
}
