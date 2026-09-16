package com.stackwork360.accessgovernanceservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AccessApprovalPolicyTest {
    @Test
    void appliesByResourceTypeAndMinimumLevel() {
        AccessApprovalPolicy policy = new AccessApprovalPolicy(null, "tenant-1", ResourceType.PRODUCTION, AccessLevel.WRITE, true, true, true);
        AccessResource resource = new AccessResource(null, "tenant-1", "prod-db", "Prod DB", ResourceType.PRODUCTION, true, "owner-1");

        assertThat(policy.appliesTo(resource, AccessLevel.ADMIN)).isTrue();
        assertThat(policy.appliesTo(resource, AccessLevel.READ)).isFalse();
    }

    @Test
    void requiresAtLeastOneApprovalGate() {
        assertThatThrownBy(() -> new AccessApprovalPolicy(null, "tenant-1", ResourceType.REPOSITORY, AccessLevel.READ, false, false, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("approval");
    }
}
