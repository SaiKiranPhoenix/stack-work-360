package com.stackwork360.tenantservice.infrastructure;

import com.stackwork360.tenantservice.domain.Tenant;
import com.stackwork360.tenantservice.domain.TenantRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTenantRepository implements TenantRepository {
    private final ConcurrentMap<UUID, Tenant> tenantsById = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, UUID> idsBySlug = new ConcurrentHashMap<>();

    @Override
    public Tenant save(Tenant tenant) {
        tenantsById.put(tenant.id(), tenant);
        idsBySlug.put(tenant.slug().toLowerCase(Locale.ROOT), tenant.id());
        return tenant;
    }

    @Override
    public Optional<Tenant> findById(UUID id) {
        return Optional.ofNullable(tenantsById.get(id));
    }

    @Override
    public Optional<Tenant> findBySlug(String slug) {
        UUID id = idsBySlug.get(slug.toLowerCase(Locale.ROOT));
        return id == null ? Optional.empty() : findById(id);
    }

    @Override
    public List<Tenant> findAll() {
        return tenantsById.values().stream()
                .sorted(Comparator.comparing(Tenant::createdAt))
                .toList();
    }

    @Override
    public boolean existsBySlug(String slug) {
        return idsBySlug.containsKey(slug.toLowerCase(Locale.ROOT));
    }
}
