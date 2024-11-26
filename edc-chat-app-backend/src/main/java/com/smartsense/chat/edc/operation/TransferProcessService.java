package com.smartsense.chat.edc.operation;

import com.smartsense.chat.dao.entity.ChatMessage;
import com.smartsense.chat.edc.client.EDCConnectorClient;
import com.smartsense.chat.edc.settings.AppConfig;
import com.smartsense.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartsense.chat.edc.constant.EdcConstant.CONTEXT;
import static com.smartsense.chat.edc.constant.EdcConstant.EDC_NS_URL;
import static com.smartsense.chat.edc.constant.EdcConstant.EQUAL;
import static com.smartsense.chat.edc.constant.EdcConstant.FILTER_EXPRESSION;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERAND_LEFT;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERAND_RIGHT;
import static com.smartsense.chat.edc.constant.EdcConstant.OPERATOR;
import static com.smartsense.chat.edc.constant.EdcConstant.TYPE;
import static com.smartsense.chat.edc.constant.EdcConstant.VOCAB;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferProcessService {

    private final EDCConnectorClient edc;
    private final AppConfig config;
    private final ChatMessageService chatMessageService;

    public String initiateTransfer(String agreementId, ChatMessage chatMessage) {
        try {
            log.info("Transfer process initiate for agreementId {}", agreementId);
            Map<String, Object> transferRequest = prepareTransferRequest(agreementId);
            // initiate the transfer process
            Thread.sleep(7_000);
            List<Map<String, Object>> transferResponse = edc.initTransferProcess(config.edc().edcUri(), transferRequest, config.edc().authCode());
            log.trace("Received transfer response -> {}", transferResponse);

            // get the transfer process id from response
            String transferProcessId = transferResponse.getFirst().get("transferProcessId").toString();
            log.info("Transfer process is complete for agreementId {} and transferId {} ", agreementId, transferProcessId);
            return transferProcessId;
        } catch (Exception ex) {
            chatMessage.setErrorDetail(String.format("Error occurred in transfer process for agreement Id %s and Exception is %s", agreementId, ex.getMessage()));
            chatMessageService.create(chatMessage);
            log.error("Error occurred in transfer process for agreement Id {}", agreementId, ex);
            return null;
        }
    }

    private Map<String, Object> prepareTransferRequest(String agreementId) {
        Map<String, Object> transferRequest = new HashMap<>();
        transferRequest.put(CONTEXT, Map.of(VOCAB, EDC_NS_URL));
        transferRequest.put(TYPE, "QuerySpec");
        transferRequest.put("offset", 0);
        transferRequest.put("limit", 1);
        transferRequest.put(FILTER_EXPRESSION, List.of(Map.of(OPERAND_LEFT, "agreementId",
                OPERATOR, EQUAL,
                OPERAND_RIGHT, agreementId)));
        log.trace("Transfer request looks like: {}", transferRequest);
        return transferRequest;
    }
}
