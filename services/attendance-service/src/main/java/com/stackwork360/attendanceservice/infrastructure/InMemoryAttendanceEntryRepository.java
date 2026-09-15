package com.stackwork360.attendanceservice.infrastructure;

import com.stackwork360.attendanceservice.domain.AttendanceEntry;
import com.stackwork360.attendanceservice.domain.AttendanceEntryRepository;
import com.stackwork360.attendanceservice.domain.AttendanceEntryStatus;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAttendanceEntryRepository implements AttendanceEntryRepository {
    private final Map<UUID, AttendanceEntry> entries = new ConcurrentHashMap<>();

    @Override
    public AttendanceEntry save(AttendanceEntry entry) {
        entries.put(entry.id(), entry);
        return entry;
    }

    @Override
    public Optional<AttendanceEntry> findById(UUID id) {
        return Optional.ofNullable(entries.get(id));
    }

    @Override
    public Optional<AttendanceEntry> findOpenEntry(String tenantId, String workerId) {
        return entries.values().stream()
                .filter(entry -> entry.tenantId().equals(tenantId))
                .filter(entry -> entry.workerId().equals(workerId))
                .filter(entry -> entry.status() == AttendanceEntryStatus.OPEN)
                .findFirst();
    }

    @Override
    public List<AttendanceEntry> findByTenantIdAndWorkerId(String tenantId, String workerId) {
        return entries.values().stream()
                .filter(entry -> entry.tenantId().equals(tenantId))
                .filter(entry -> entry.workerId().equals(workerId))
                .sorted(Comparator.comparing(AttendanceEntry::checkInAt).reversed())
                .toList();
    }

    @Override
    public List<AttendanceEntry> findByTenantIdAndRange(String tenantId, Instant from, Instant to) {
        return entries.values().stream()
                .filter(entry -> entry.tenantId().equals(tenantId))
                .filter(entry -> from == null || !entry.checkInAt().isBefore(from))
                .filter(entry -> to == null || !entry.checkInAt().isAfter(to))
                .sorted(Comparator.comparing(AttendanceEntry::checkInAt).reversed())
                .toList();
    }
}
