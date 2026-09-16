package com.stackwork360.accessgovernanceservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AccessRequestTest {
    @Test
    void movesThroughApprovalProvisionAndRemoval() {
        AccessRequest request = request();

        request.approve("valid business need");
        request.provision();
        request.requestRemoval("offboarded");
        request.markRemoved();

        assertThat(request.status()).isEqualTo(AccessRequestStatus.REMOVED);
    }

    @Test
    void blocksProvisionWithoutApproval() {
        assertThatThrownBy(() -> request().provision())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("approved");
    }

    private AccessRequest request() {
        return new AccessRequest(null, "tenant-1", "worker-1", "repo-payroll", AccessLevel.WRITE, "incident support", AccessRequestStatus.REQUESTED, null, null, null);
    }
}
