package com.smartsense.chat.edc.manager;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
