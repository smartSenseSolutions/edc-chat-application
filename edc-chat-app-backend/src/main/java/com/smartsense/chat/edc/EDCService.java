package com.smartsense.chat.edc;

import com.smartsense.chat.edc.operation.PublicUrlHandlerService;
import com.smartsense.chat.edc.operation.TransferProcessService;
import com.smartsense.chat.utils.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class EDCService {


    private final TransferProcessService transferProcessService;
    private final PublicUrlHandlerService publicUrlHandlerService;

    public void initProcess(URI edcUri, ChatMessage chatMessage) {
        // TODO query catalog
        //TODO start negotiation
        //TODO get agreement


        String agreementId = null;

        // Initiate the transfer process
        String transferProcessId = transferProcessService.initiateTransfer(edcUri, agreementId);

        // sent the message to public url
        publicUrlHandlerService.getAuthCodeAndPublicUrl(edcUri, transferProcessId, chatMessage);
    }


}
