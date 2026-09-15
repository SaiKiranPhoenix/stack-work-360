package com.stackwork360.peoplecoreservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkerRepository {
    Worker save(Worker worker);

    Optional<Worker> findById(UUID id);

    Optional<Worker> findByTenantIdAndEmployeeNumber(String tenantId, String employeeNumber);

    Optional<Worker> findByTenantIdAndWorkEmail(String tenantId, String workEmail);

    List<Worker> findByTenantId(String tenantId);

    boolean existsByTenantIdAndEmployeeNumber(String tenantId, String employeeNumber);

    boolean existsByTenantIdAndWorkEmail(String tenantId, String workEmail);
}
