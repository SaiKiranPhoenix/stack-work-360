package com.stackwork360.payrollprepservice.infrastructure;

import com.stackwork360.payrollprepservice.domain.PayrollPeriod;
import com.stackwork360.payrollprepservice.domain.PayrollPeriodRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPayrollPeriodRepository implements PayrollPeriodRepository {
    private final Map<UUID, PayrollPeriod> periods = new ConcurrentHashMap<>();

    @Override
    public PayrollPeriod save(PayrollPeriod period) {
        periods.put(period.id(), period);
        return period;
    }

    @Override
    public Optional<PayrollPeriod> findById(UUID id) {
        return Optional.ofNullable(periods.get(id));
    }

    @Override
    public List<PayrollPeriod> findByTenantId(String tenantId) {
        return periods.values().stream()
                .filter(period -> period.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(PayrollPeriod::startDate).thenComparing(PayrollPeriod::endDate))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
