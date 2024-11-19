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
package com.smartsense.service.impl;

import com.smartsense.api.model.request.UserRequest;
import com.smartsense.api.model.response.PageResponse;
import com.smartsense.api.model.response.UserResponse;
import com.smartsense.dao.entity.UserMaster;
import com.smartsense.service.UserManagementService;
import com.smartsense.service.entity.BaseService;
import com.smartsense.service.entity.UserMasterService;
import com.smartsensesolutions.commons.dao.filter.FilterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * User management service Implementation
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserManagementServiceImpl extends BaseService implements UserManagementService {

    private final UserMasterService userMasterService;

    @Override
    public UserResponse getUserById(UUID id) {
        return toType(userMasterService.get(id), UserResponse.class);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        return toType(userMasterService.createUser(userRequest), UserResponse.class);
    }

    @Override
    public PageResponse<UserResponse> userFilter(FilterRequest filterRequest) {
        Page<UserMaster> filter = userMasterService.filter(filterRequest);
        return toPageResponse(filter, filterRequest, UserResponse.class);
    }
}
