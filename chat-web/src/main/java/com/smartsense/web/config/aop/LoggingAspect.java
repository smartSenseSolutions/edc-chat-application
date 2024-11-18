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
package com.smartsense.web.config.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Logging aspect that logs every request and response
 *
 * @author Sunil Kanzar
 * @since 14th feb 2024
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {


    private final HttpServletRequest request;

    @Pointcut("execution(* ss.mod.demo.resource.*.*(..))")
    public void printLogs() {
    }

    @SneakyThrows
    @Around("printLogs()")
    public Object logMethod(ProceedingJoinPoint joinPoint) {
        String targetClass = joinPoint.getTarget().getClass().getSimpleName();
        String targetMethod = joinPoint.getSignature().getName();
        Thread currentThread = Thread.currentThread();
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        log.info("==> Request : {}, Class: {}, method(s): {}, Thread :{}, IpAddress :{} ",
                request.getRequestURL(), targetClass, targetMethod, currentThread.threadId(), ipAddress);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object response;
        try {
            response = joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("<== Responding in time:{} ms, Thread :{} ", stopWatch.getTotalTimeMillis(), currentThread.threadId());
        }
        return response;
    }
}
