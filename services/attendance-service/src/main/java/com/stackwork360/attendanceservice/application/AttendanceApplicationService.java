package com.stackwork360.attendanceservice.application;

import com.stackwork360.attendanceservice.domain.AttendanceEntry;
import com.stackwork360.attendanceservice.domain.AttendanceEntryRepository;
import com.stackwork360.attendanceservice.domain.AttendanceSummary;
import com.stackwork360.web.ResourceNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AttendanceApplicationService {
    private final AttendanceEntryRepository attendanceEntryRepository;

    public AttendanceApplicationService(AttendanceEntryRepository attendanceEntryRepository) {
        this.attendanceEntryRepository = attendanceEntryRepository;
    }

    public AttendanceEntry checkIn(CheckInCommand command) {
        attendanceEntryRepository.findOpenEntry(command.tenantId(), command.workerId())
                .ifPresent(entry -> {
                    throw new IllegalStateException("worker already has an open attendance entry");
                });
        return attendanceEntryRepository.save(AttendanceEntry.checkIn(
                command.tenantId(),
                command.workerId(),
                command.type(),
                command.checkInAt(),
                command.location()
        ));
    }

    public AttendanceEntry checkOut(String tenantId, String workerId, CheckOutCommand command) {
        AttendanceEntry entry = attendanceEntryRepository.findOpenEntry(tenantId, workerId)
                .orElseThrow(() -> new ResourceNotFoundException("open attendance entry not found"));
        entry.checkOut(command.checkOutAt());
        return attendanceEntryRepository.save(entry);
    }

    public AttendanceEntry recordRemoteWork(RemoteWorkCommand command) {
        return attendanceEntryRepository.save(AttendanceEntry.remoteWork(
                command.tenantId(),
                command.workerId(),
                command.startAt(),
                command.endAt(),
                command.location()
        ));
    }

    public AttendanceEntry correct(UUID entryId, CorrectAttendanceCommand command) {
        AttendanceEntry entry = get(entryId);
        entry.correct(command.checkInAt(), command.checkOutAt(), command.type(), command.location(), command.reason());
        return attendanceEntryRepository.save(entry);
    }

    public AttendanceEntry get(UUID entryId) {
        return attendanceEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("attendance entry not found"));
    }

    public List<AttendanceEntry> workerEntries(String tenantId, String workerId) {
        return attendanceEntryRepository.findByTenantIdAndWorkerId(tenantId, workerId);
    }

    public List<AttendanceEntry> range(String tenantId, Instant from, Instant to) {
        return attendanceEntryRepository.findByTenantIdAndRange(tenantId, from, to);
    }

    public AttendanceSummary summary(UUID entryId, Instant now) {
        return get(entryId).summary(now);
    }
}
