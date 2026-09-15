package com.stackwork360.developerintelligenceservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeveloperAccessRequestTest {
    @Test
    void approvesRequestedAccess() {
        DeveloperAccessRequest request = request();

        request.approve("lead-1");

        assertEquals(DeveloperAccessStatus.APPROVED, request.status());
        assertEquals("lead-1", request.decidedBy());
    }

    @Test
    void rejectsDoubleDecision() {
        DeveloperAccessRequest request = request();
        request.reject("lead-1");

        assertThrows(IllegalStateException.class, () -> request.approve("lead-2"));
    }

    @Test
    void onlyApprovedAccessCanBeRevoked() {
        DeveloperAccessRequest request = request();

        assertThrows(IllegalStateException.class, () -> request.revoke("lead-1"));
    }

    private static DeveloperAccessRequest request() {
        return DeveloperAccessRequest.request(
                "tenant-1",
                UUID.randomUUID(),
                "dev-1",
                DeveloperAccessLevel.WRITE,
                "Join on-call rotation"
        );
    }
}
