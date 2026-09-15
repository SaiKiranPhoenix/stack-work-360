package com.stackwork360.shiftschedulingservice.infrastructure;

import com.stackwork360.shiftschedulingservice.domain.ShiftAssignment;
import com.stackwork360.shiftschedulingservice.domain.ShiftAssignmentRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryShiftAssignmentRepository implements ShiftAssignmentRepository {
    private final Map<UUID, ShiftAssignment> assignments = new ConcurrentHashMap<>();

    @Override
    public ShiftAssignment save(ShiftAssignment assignment) {
        assignments.put(assignment.id(), assignment);
        return assignment;
    }

    @Override
    public Optional<ShiftAssignment> findById(UUID id) {
        return Optional.ofNullable(assignments.get(id));
    }

    @Override
    public List<ShiftAssignment> findByTenantIdAndDate(String tenantId, LocalDate date) {
        return assignments.values().stream()
                .filter(assignment -> assignment.tenantId().equals(tenantId))
                .filter(assignment -> assignment.shiftDate().equals(date))
                .sorted(Comparator.comparing(ShiftAssignment::startsAt))
                .toList();
    }

    @Override
    public List<ShiftAssignment> findByTenantIdAndWorkerId(String tenantId, String workerId) {
        return assignments.values().stream()
                .filter(assignment -> assignment.tenantId().equals(tenantId))
                .filter(assignment -> assignment.workerId().equals(workerId))
                .sorted(Comparator.comparing(ShiftAssignment::startsAt))
                .toList();
    }
}
