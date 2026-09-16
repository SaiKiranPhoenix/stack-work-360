package com.stackwork360.benefitsservice.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OpenEnrollmentWindowRepository {
    OpenEnrollmentWindow save(OpenEnrollmentWindow window);
    Optional<OpenEnrollmentWindow> activeWindow(String tenantId, LocalDate date);
    List<OpenEnrollmentWindow> findByTenantId(String tenantId);
}
