package com.stackwork360.tenantservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stackwork360.tenantservice.domain.DataResidencyRegion;
import com.stackwork360.tenantservice.domain.Tenant;
import com.stackwork360.tenantservice.domain.TenantPlan;
import com.stackwork360.tenantservice.domain.TenantStatus;
import com.stackwork360.tenantservice.infrastructure.InMemoryTenantRepository;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TenantApplicationServiceTest {
    private final TenantApplicationService service = new TenantApplicationService(new InMemoryTenantRepository());

    @Test
    void createsTenant() {
        Tenant tenant = service.create(new CreateTenantCommand(
                "acme-platform",
                "Acme Platform",
                TenantPlan.ENTERPRISE,
                DataResidencyRegion.US,
                365,
                Set.of("risk-engine"),
                Map.of("timezone", "UTC")
        ));

        assertEquals("acme-platform", tenant.slug());
        assertEquals(TenantStatus.ACTIVE, tenant.status());
    }

    @Test
    void rejectsDuplicateSlug() {
        CreateTenantCommand command = new CreateTenantCommand(
                "duplicate-co",
                "Duplicate Co",
                TenantPlan.STARTUP,
                DataResidencyRegion.EU,
                365,
                Set.of(),
                Map.of()
        );

        service.create(command);

        assertThrows(IllegalArgumentException.class, () -> service.create(command));
    }

    @Test
    void updatesTenantSettingsWithoutChangingIdentity() {
        Tenant tenant = service.create(new CreateTenantCommand(
                "changeable-co",
                "Changeable Co",
                TenantPlan.STARTUP,
                DataResidencyRegion.APAC,
                365,
                Set.of(),
                Map.of()
        ));

        Tenant updated = service.update(tenant.id(), new UpdateTenantCommand(
                "Changeable Enterprise",
                TenantPlan.ENTERPRISE,
                DataResidencyRegion.INDIA,
                730,
                Set.of("developer-intelligence", "workforce-planning"),
                Map.of("timezone", "Asia/Kolkata")
        ));

        assertEquals(tenant.id(), updated.id());
        assertEquals("changeable-co", updated.slug());
        assertEquals("Changeable Enterprise", updated.displayName());
        assertEquals(TenantPlan.ENTERPRISE, updated.plan());
        assertEquals(DataResidencyRegion.INDIA, updated.dataResidencyRegion());
        assertEquals(730, updated.retentionDays());
    }

    @Test
    void assignsFeatureFlagsAndResolvesEntitlements() {
        Tenant tenant = service.create(new CreateTenantCommand(
                "entitled-co",
                "Entitled Co",
                TenantPlan.STARTUP,
                DataResidencyRegion.US,
                365,
                Set.of(),
                Map.of()
        ));

        service.assignFeatures(tenant.id(), new AssignTenantFeaturesCommand(Set.of("risk-engine")));
        var entitlements = service.entitlements(tenant.id());

        assertEquals(TenantPlan.STARTUP, entitlements.plan());
        assertEquals(true, entitlements.entitledTo("risk-engine"));
        assertEquals(true, entitlements.entitledTo("people-core"));
        assertEquals(Set.of("risk-engine"), entitlements.assignedFeatures());
    }
}
