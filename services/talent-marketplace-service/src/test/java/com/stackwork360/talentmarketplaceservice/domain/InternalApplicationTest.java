package com.stackwork360.talentmarketplaceservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class InternalApplicationTest {
    @Test
    void requiresManagerApprovalBeforeAcceptance() {
        InternalApplication application = application();

        assertThatThrownBy(() -> application.accept("accepted by owner"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("manager approval");
    }

    @Test
    void approvesAndAcceptsApplication() {
        InternalApplication application = application();

        application.managerApprove("capacity approved");
        application.accept("owner accepted");

        assertThat(application.status()).isEqualTo(ApplicationStatus.ACCEPTED);
    }

    private InternalApplication application() {
        return new InternalApplication(null, "tenant-1", UUID.randomUUID(), "worker-1", "manager-1", "ready for stretch", ApplicationStatus.SUBMITTED, null, null, null);
    }
}
