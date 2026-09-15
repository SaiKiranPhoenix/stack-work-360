package com.stackwork360.tenantservice.application;

import com.stackwork360.tenantservice.domain.Tenant;
import com.stackwork360.tenantservice.domain.TenantEntitlements;
import com.stackwork360.tenantservice.domain.TenantPlan;
import com.stackwork360.tenantservice.domain.TenantRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TenantApplicationService {
    private final TenantRepository tenantRepository;

    public TenantApplicationService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public Tenant create(CreateTenantCommand command) {
        if (tenantRepository.existsBySlug(command.slug().toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("tenant slug is already in use");
        }

        Tenant tenant = Tenant.provision(
                command.slug(),
                command.displayName(),
                command.plan(),
                command.dataResidencyRegion(),
                command.retentionDays(),
                command.enabledFeatures(),
                command.configuration()
        );
        return tenantRepository.save(tenant);
    }

    public Tenant update(UUID tenantId, UpdateTenantCommand command) {
        Tenant tenant = get(tenantId);
        tenant.updateSettings(
                command.displayName(),
                command.plan(),
                command.dataResidencyRegion(),
                command.retentionDays(),
                command.enabledFeatures(),
                command.configuration()
        );
        return tenantRepository.save(tenant);
    }

    public Tenant suspend(UUID tenantId) {
        Tenant tenant = get(tenantId);
        tenant.suspend();
        return tenantRepository.save(tenant);
    }

    public Tenant reactivate(UUID tenantId) {
        Tenant tenant = get(tenantId);
        tenant.reactivate();
        return tenantRepository.save(tenant);
    }

    public Tenant assignFeatures(UUID tenantId, AssignTenantFeaturesCommand command) {
        Tenant tenant = get(tenantId);
        tenant.assignFeatures(command.enabledFeatures());
        return tenantRepository.save(tenant);
    }

    public TenantEntitlements entitlements(UUID tenantId) {
        Tenant tenant = get(tenantId);
        Set<String> planFeatures = planFeatures(tenant.plan());
        Set<String> entitledFeatures = new java.util.TreeSet<>(planFeatures);
        entitledFeatures.addAll(tenant.enabledFeatures());
        return new TenantEntitlements(tenant.plan(), Set.copyOf(entitledFeatures), tenant.enabledFeatures());
    }

    public Tenant get(UUID tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("tenant not found"));
    }

    public List<Tenant> list() {
        return tenantRepository.findAll();
    }

    private static Set<String> planFeatures(TenantPlan plan) {
        return switch (plan) {
            case FREE_TRIAL -> Set.of("people-core", "organization", "leave", "document", "helpdesk");
            case STARTUP -> Set.of(
                    "people-core",
                    "organization",
                    "workflow",
                    "leave",
                    "document",
                    "helpdesk",
                    "payroll-prep",
                    "notification",
                    "analytics"
            );
            case ENTERPRISE -> Set.of(
                    "people-core",
                    "organization",
                    "workflow",
                    "leave",
                    "document",
                    "helpdesk",
                    "payroll-prep",
                    "developer-intelligence",
                    "risk-engine",
                    "notification",
                    "audit",
                    "analytics",
                    "integration",
                    "access-governance",
                    "workforce-planning"
            );
        };
    }
}
