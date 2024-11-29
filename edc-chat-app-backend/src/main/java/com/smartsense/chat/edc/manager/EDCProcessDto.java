/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.edc.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EDCProcessDto {

    private String receiverBpn;
    private String dspUrl;
    private String offerId;
    private String negotiationId;
    private String agreementId;
    private String transferProcessId;
    private String errorDetail;
}
