package com.stackwork360.workforceplanningservice.domain;

import java.util.List;

public interface HiringPlanRepository {
    HiringPlan save(HiringPlan plan);
    List<HiringPlan> findByTenantId(String tenantId);
}
