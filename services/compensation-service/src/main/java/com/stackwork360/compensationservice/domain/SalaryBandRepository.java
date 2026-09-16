package com.stackwork360.compensationservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryBandRepository {
    SalaryBand save(SalaryBand band);
    Optional<SalaryBand> findById(UUID id);
    Optional<SalaryBand> find(String tenantId, String jobLevel, String location);
    List<SalaryBand> findByTenantId(String tenantId);
}
