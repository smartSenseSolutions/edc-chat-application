package com.smartsense.chat.utils.response;

import java.util.UUID;

public record BusinessPartnerResponse(UUID id,
                                      String name,
                                      String bpn,
                                      String edcUrl) {
}
