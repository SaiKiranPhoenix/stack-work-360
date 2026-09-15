package com.stackwork360.leaveservice.infrastructure;

import com.stackwork360.leaveservice.domain.LeaveBalance;
import com.stackwork360.leaveservice.domain.LeaveBalanceRepository;
import com.stackwork360.leaveservice.domain.LeaveType;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLeaveBalanceRepository implements LeaveBalanceRepository {
    private final ConcurrentMap<String, LeaveBalance> balancesByKey = new ConcurrentHashMap<>();

    @Override
    public LeaveBalance save(LeaveBalance balance) {
        balancesByKey.put(key(balance.tenantId(), balance.workerId(), balance.leaveType()), balance);
        return balance;
    }

    @Override
    public Optional<LeaveBalance> find(String tenantId, String workerId, LeaveType leaveType) {
        return Optional.ofNullable(balancesByKey.get(key(tenantId, workerId, leaveType)));
    }

    @Override
    public List<LeaveBalance> findByTenantIdAndWorkerId(String tenantId, String workerId) {
        return balancesByKey.values().stream()
                .filter(balance -> balance.tenantId().equals(tenantId))
                .filter(balance -> balance.workerId().equals(workerId))
                .toList();
    }

    private static String key(String tenantId, String workerId, LeaveType leaveType) {
        return tenantId + ":" + workerId + ":" + leaveType.name();
    }
}
