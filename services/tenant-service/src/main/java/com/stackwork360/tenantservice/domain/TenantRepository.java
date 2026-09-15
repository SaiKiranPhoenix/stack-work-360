package com.stackwork360.tenantservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {
    Tenant save(Tenant tenant);

    Optional<Tenant> findById(UUID id);

    Optional<Tenant> findBySlug(String slug);

    List<Tenant> findAll();

    boolean existsBySlug(String slug);
}
