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
package com.smartsense.service.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.smartsensesolutions.commons.dao.filter.FilterRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.smartsense.api.constant.ContField;
import com.smartsense.api.model.response.PageResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Common Service util for all the kind of service
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
public interface ServiceUtil {
    ObjectMapper getObjectMapper();

    MessageSource getMessageSource();

    /**
     * Instantiates a new Base service.
     */

    default <I, O> O toType(I data, Class<O> c) {
        return getObjectMapper().convertValue(data, c);
    }

    default <I, O> List<O> toListOf(Collection<I> data, Class<O> c) {
        return getObjectMapper().convertValue(data, TypeFactory.defaultInstance().constructParametricType(List.class, c));
    }

    default <I, O> List<O> toListOf(Collection<I> data, Function<I, O> converter) {
        return data.stream().map(converter).toList();
    }

    default <I, O> PageResponse<O> toPageResponse(Page<I> page, FilterRequest filterRequest, Class<O> clazz) {
        PageResponse.Pageable pageable = PageResponse.Pageable.builder().totalPages(page.getTotalPages()).numberOfElements(page.getNumberOfElements()).totalElements(page.getTotalElements()).pageNumber(filterRequest.getPage()).pageSize(filterRequest.getSize()).build();
        List<O> list = toListOf(page.getContent(), clazz);
        return PageResponse.of(list, pageable, filterRequest.getSort());
    }

    default <I, O> PageResponse<O> toPageResponse(Page<I> page, FilterRequest filterRequest, Function<I, O> converter) {
        PageResponse.Pageable pageable = PageResponse.Pageable.builder().totalPages(page.getTotalPages()).numberOfElements(page.getNumberOfElements()).totalElements(page.getTotalElements()).pageNumber(filterRequest.getPage()).pageSize(filterRequest.getSize()).build();
        List<O> list = new ArrayList<>();
        if (page.hasContent()) {
            list = page.getContent().stream().map(converter).toList();
        }
        return PageResponse.of(list, pageable, filterRequest.getSort());
    }

    /**
     * To String
     *
     * @param <I>  input type
     * @param data input data
     * @return converted string
     */
    default <I> String convertToString(I data) {
        try {
            return getObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Resolve string.
     *
     * @param key the key
     * @param arg the arg
     * @return the string
     */
    default String resolveMessage(String key, String... arg) {
        try {
            return getMessageSource().getMessage(key, arg, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }

    default String getUserEmail() {
        Map<?, ?> claims = getClaims();
        return (String) claims.get(ContField.EMAIL);
    }

    private Map<?, ?> getClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new HashMap<>();
        }
        Map<?, ?> credInfo = toType(authentication.getCredentials(), Map.class);
        return toType(credInfo.get(ContField.CLAIMS), Map.class);
    }


    /**
     * Delete the created tmp files, if exists.
     *
     * @param files - Indicates the files
     */
    default void delete(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
