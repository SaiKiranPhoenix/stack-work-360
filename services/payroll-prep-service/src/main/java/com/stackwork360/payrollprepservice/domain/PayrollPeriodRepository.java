package com.stackwork360.payrollprepservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayrollPeriodRepository {
    PayrollPeriod save(PayrollPeriod period);

    Optional<PayrollPeriod> findById(UUID id);

    List<PayrollPeriod> findByTenantId(String tenantId);
}
