package com.stackwork360.benefitsservice.domain;

import java.util.List;
import java.util.Optional;

public interface EmployeeEnrollmentRepository {
    EmployeeEnrollment save(EmployeeEnrollment enrollment);
    Optional<EmployeeEnrollment> findActive(String tenantId, String workerId, String planCode);
    List<EmployeeEnrollment> findByTenantId(String tenantId);
    List<EmployeeEnrollment> findByWorker(String tenantId, String workerId);
}
