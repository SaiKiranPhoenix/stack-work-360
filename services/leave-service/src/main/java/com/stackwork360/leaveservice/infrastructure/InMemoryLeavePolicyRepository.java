package com.stackwork360.leaveservice.infrastructure;

import com.stackwork360.leaveservice.domain.LeavePolicy;
import com.stackwork360.leaveservice.domain.LeavePolicyRepository;
import com.stackwork360.leaveservice.domain.LeaveType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLeavePolicyRepository implements LeavePolicyRepository {
    private final ConcurrentMap<String, LeavePolicy> policiesByKey = new ConcurrentHashMap<>();

    public InMemoryLeavePolicyRepository() {
        seed("tenant-1");
    }

    @Override
    public LeavePolicy save(LeavePolicy policy) {
        policiesByKey.put(key(policy.tenantId(), policy.leaveType()), policy);
        return policy;
    }

    @Override
    public Optional<LeavePolicy> find(String tenantId, LeaveType leaveType) {
        Optional<LeavePolicy> policy = Optional.ofNullable(policiesByKey.get(key(tenantId, leaveType)));
        if (policy.isEmpty()) {
            seed(tenantId);
            policy = Optional.ofNullable(policiesByKey.get(key(tenantId, leaveType)));
        }
        return policy;
    }

    @Override
    public List<LeavePolicy> findByTenantId(String tenantId) {
        seed(tenantId);
        return policiesByKey.values().stream()
                .filter(policy -> policy.tenantId().equals(tenantId))
                .toList();
    }

    private void seed(String tenantId) {
        save(new LeavePolicy(tenantId, LeaveType.VACATION, BigDecimal.valueOf(18), true, true));
        save(new LeavePolicy(tenantId, LeaveType.SICK, BigDecimal.valueOf(10), true, true));
        save(new LeavePolicy(tenantId, LeaveType.PERSONAL, BigDecimal.valueOf(5), true, true));
        save(new LeavePolicy(tenantId, LeaveType.UNPAID, BigDecimal.valueOf(365), true, false));
    }

    private static String key(String tenantId, LeaveType leaveType) {
        return tenantId + ":" + leaveType.name();
    }
}
