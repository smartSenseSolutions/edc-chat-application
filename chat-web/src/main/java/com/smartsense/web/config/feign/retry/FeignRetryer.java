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
package com.smartsense.web.config.feign.retry;

import feign.RetryableException;
import feign.Retryer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign Retry implementation
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@Slf4j
@RequiredArgsConstructor
public class FeignRetryer implements Retryer {
    private final long maxRetryAttempt;
    private final long retryInterval;
    private int attempt = 1;

    @Override
    public void continueOrPropagate(RetryableException e) {
        FeignRetryer.log.info("Feign retry attempt {} due to {} ", attempt, e.getMessage());
        if (attempt++ >= maxRetryAttempt) {
            throw new RuntimeException("Max retry attempt exceed");
        }
        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new FeignRetryer(maxRetryAttempt, retryInterval);
    }
}
