package com.stackwork360.documentservice.api;

import jakarta.validation.constraints.NotBlank;

public record AcknowledgePolicyRequest(
        @NotBlank
        String workerId,

        String ipAddress
) {
}
