package com.stackwork360.documentservice.api;

import com.stackwork360.documentservice.domain.PolicyAcknowledgement;
import java.time.Instant;
import java.util.UUID;

public record PolicyAcknowledgementResponse(
        UUID id,
        UUID documentId,
        String workerId,
        Instant acknowledgedAt,
        String ipAddress
) {
    public static PolicyAcknowledgementResponse from(PolicyAcknowledgement acknowledgement) {
        return new PolicyAcknowledgementResponse(
                acknowledgement.id(),
                acknowledgement.documentId(),
                acknowledgement.workerId(),
                acknowledgement.acknowledgedAt(),
                acknowledgement.ipAddress()
        );
    }
}
