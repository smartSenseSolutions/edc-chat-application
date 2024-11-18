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

package com.smartsense.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.BeforeTransaction;
import com.smartsense.MainApplication;
import com.smartsense.api.constant.ContURI;
import com.smartsense.api.model.request.UserRequest;
import com.smartsense.api.model.response.ResponseBody;
import com.smartsense.api.model.response.UserResponse;
import com.smartsense.dao.repository.UserMasterRepository;
import com.smartsense.util.ContainerContextInitializer;
import com.smartsense.util.TestHelper;
import com.smartsense.util.constant.TestDataUtil;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { MainApplication.class })
@ActiveProfiles("test")
@ContextConfiguration(initializers = { ContainerContextInitializer.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserManagementResourceTest {


    @Autowired
    UserMasterRepository userMasterRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;


    @BeforeAll
    public final void onlyOnce() {

    }

    /**
     * This method execute before every test method execute and before any transaction in database happen and create company.
     */
    @BeforeEach
    @BeforeTransaction
    public final void before() {
        userMasterRepository.deleteAll();
    }

    @Test
    void createUser_200() {
        ResponseEntity<ResponseBody<UserResponse>> responseEntity = createUser(TestDataUtil.getUserRequest());
        Assertions.assertNotNull(responseEntity);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertNotNull(responseEntity.getBody().getBody());
        Assertions.assertNotNull(responseEntity.getBody().getBody().id());
    }


    private ResponseEntity<ResponseBody<UserResponse>> createUser(UserRequest userRequest) {
        return restTemplate.exchange(ContURI.USER, HttpMethod.POST, new HttpEntity<>(userRequest, TestHelper.prepareHeader(true)),
                new ParameterizedTypeReference<>() {
                });
    }

}
