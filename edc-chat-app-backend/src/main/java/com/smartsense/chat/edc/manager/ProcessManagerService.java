/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.manager;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ProcessManagerService {
    private final Map<String, EDCProcessDto> processManager = new HashMap<>();

    public EDCProcessDto getProcess(String bpn) {
        return processManager.get(bpn);
    }

    public void put(String bpn, EDCProcessDto dto) {
        processManager.put(bpn, dto);
    }
}
