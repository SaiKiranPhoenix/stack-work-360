package com.stackwork360.attendanceservice.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceEntryRepository {
    AttendanceEntry save(AttendanceEntry entry);

    Optional<AttendanceEntry> findById(UUID id);

    Optional<AttendanceEntry> findOpenEntry(String tenantId, String workerId);

    List<AttendanceEntry> findByTenantIdAndWorkerId(String tenantId, String workerId);

    List<AttendanceEntry> findByTenantIdAndRange(String tenantId, Instant from, Instant to);
}
