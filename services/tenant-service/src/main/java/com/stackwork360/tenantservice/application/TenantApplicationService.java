package com.stackwork360.tenantservice.application;

import com.stackwork360.tenantservice.domain.Tenant;
import com.stackwork360.tenantservice.domain.TenantRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.Locale;
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

    public Tenant get(UUID tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("tenant not found"));
    }

    public List<Tenant> list() {
        return tenantRepository.findAll();
    }
}
