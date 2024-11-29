/*
 * Copyright (c)  2024 smartSense Consulting Solutions Pvt. Ltd.
 */

package com.smartsense.chat.utils.response;

public record ChatResponse(String receiver, String sender, String content, long timestamp) {
}
