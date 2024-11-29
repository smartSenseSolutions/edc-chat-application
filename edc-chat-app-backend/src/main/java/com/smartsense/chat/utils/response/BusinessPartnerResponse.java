/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.utils.response;

import java.util.UUID;

public record BusinessPartnerResponse(UUID id,
                                      String name,
                                      String bpn,
                                      String edcUrl) {
}
