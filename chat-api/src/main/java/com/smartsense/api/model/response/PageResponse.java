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

package com.smartsense.api.model.response;


import com.smartsensesolutions.commons.dao.filter.FilterRequest;
import com.smartsensesolutions.commons.dao.filter.sort.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Common response format.
 * PageResponse Used for any paginated response.
 *
 * @param <T> - Indicates any type of response
 * @author Sunil Kanzar
 * @since 29th of Dec 2023
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -4413657906050327207L;

    private Iterable<T> content;
    private Pageable pageable;

    public static <C> PageResponse<C> of(Collection<C> data, Pageable pageable, List<Sort> sort) {
        return new PageResponse<>(data, pageable.toBuilder().sort(sort).build());
    }

    public static <C> PageResponse<C> of(Page<C> page, List<Sort> sort) {
        return of(page.getContent(), page.getNumber(), page.getNumberOfElements(), page.getSize(), page.getTotalPages(), page.getTotalElements(), sort);
    }

    public static <C> PageResponse<C> of(Iterable<C> content, Integer currentPage, Integer numberOfElement, Integer size, Integer totalPages, Long totalElements, List<Sort> sort) {
        PageResponse<C> page = new PageResponse<>();
        page.content = content;
        page.setPageable(new Pageable(size, totalPages, currentPage, numberOfElement, totalElements, sort));
        return page;
    }

    public static <T> PageResponse<T> getBlank(FilterRequest filter) {
        Pageable pageable = Pageable.builder()
                .totalPages(0)
                .numberOfElements(0)
                .totalElements(0)
                .pageNumber(0)
                .pageSize(filter.getSize()).build();
        return PageResponse.of(new ArrayList<>(), pageable, filter.getSort());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder(toBuilder = true)
    public static class Pageable implements Serializable {
        @Serial
        private static final long serialVersionUID = -4223261549345391709L;

        private int pageSize;
        private int totalPages;
        private int pageNumber;
        private int numberOfElements;
        private long totalElements;
        private List<Sort> sort;
    }
}
