package com.stackwork360.tenantservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TenantTest {
    @Test
    void provisionsActiveTenantWithConfiguredPlanRegionAndRetention() {
        Tenant tenant = Tenant.provision(
                "acme-tech",
                "Acme Tech",
                TenantPlan.ENTERPRISE,
                DataResidencyRegion.INDIA,
                365,
                Set.of("developer-intelligence"),
                Map.of("timezone", "Asia/Kolkata")
        );

        assertEquals("acme-tech", tenant.slug());
        assertEquals("Acme Tech", tenant.displayName());
        assertEquals(TenantStatus.ACTIVE, tenant.status());
        assertEquals(TenantPlan.ENTERPRISE, tenant.plan());
        assertEquals(DataResidencyRegion.INDIA, tenant.dataResidencyRegion());
        assertEquals(365, tenant.retentionDays());
    }

    @Test
    void rejectsInvalidSlug() {
        assertThrows(IllegalArgumentException.class, () -> Tenant.provision(
                "Bad Slug",
                "Bad Slug Inc",
                TenantPlan.STARTUP,
                DataResidencyRegion.US,
                365,
                Set.of(),
                Map.of()
        ));
    }

    @Test
    void rejectsRetentionOutsideAllowedWindow() {
        assertThrows(IllegalArgumentException.class, () -> Tenant.provision(
                "short-retention",
                "Short Retention Inc",
                TenantPlan.STARTUP,
                DataResidencyRegion.US,
                7,
                Set.of(),
                Map.of()
        ));
    }

    @Test
    void supportsSuspendAndReactivateLifecycle() {
        Tenant tenant = Tenant.provision(
                "lifecycle-co",
                "Lifecycle Co",
                TenantPlan.STARTUP,
                DataResidencyRegion.EU,
                365,
                Set.of(),
                Map.of()
        );

        tenant.suspend();
        assertEquals(TenantStatus.SUSPENDED, tenant.status());

        tenant.reactivate();
        assertEquals(TenantStatus.ACTIVE, tenant.status());
    }

    @Test
    void assignsFeatureFlagsWithoutChangingPlan() {
        Tenant tenant = Tenant.provision(
                "feature-co",
                "Feature Co",
                TenantPlan.STARTUP,
                DataResidencyRegion.US,
                365,
                Set.of(),
                Map.of()
        );

        tenant.assignFeatures(Set.of("risk-engine", "developer-intelligence"));

        assertEquals(TenantPlan.STARTUP, tenant.plan());
        assertEquals(Set.of("risk-engine", "developer-intelligence"), tenant.enabledFeatures());
    }
}
