package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.application.AttendanceApplicationService;
import com.stackwork360.common.CorrelationIds;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance/v1")
public class AttendanceController {
    private final AttendanceApplicationService attendanceApplicationService;

    public AttendanceController(AttendanceApplicationService attendanceApplicationService) {
        this.attendanceApplicationService = attendanceApplicationService;
    }

    @PostMapping("/check-ins")
    public ResponseEntity<AttendanceEntryResponse> checkIn(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CheckInRequest request
    ) {
        AttendanceEntryResponse response = AttendanceEntryResponse.from(attendanceApplicationService.checkIn(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/attendance/v1/entries/" + response.id()))
                .body(response);
    }

    @PatchMapping("/workers/{workerId}/check-out")
    public AttendanceEntryResponse checkOut(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId,
            @Valid @RequestBody CheckOutRequest request
    ) {
        return AttendanceEntryResponse.from(attendanceApplicationService.checkOut(tenantId, workerId, request.toCommand()));
    }

    @PostMapping("/remote-work")
    public ResponseEntity<AttendanceEntryResponse> remoteWork(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RemoteWorkRequest request
    ) {
        AttendanceEntryResponse response = AttendanceEntryResponse.from(attendanceApplicationService.recordRemoteWork(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/attendance/v1/entries/" + response.id()))
                .body(response);
    }

    @PatchMapping("/entries/{entryId}/correction")
    public AttendanceEntryResponse correct(
            @PathVariable UUID entryId,
            @Valid @RequestBody CorrectAttendanceRequest request
    ) {
        return AttendanceEntryResponse.from(attendanceApplicationService.correct(entryId, request.toCommand()));
    }

    @GetMapping("/entries/{entryId}")
    public AttendanceEntryResponse get(@PathVariable UUID entryId) {
        return AttendanceEntryResponse.from(attendanceApplicationService.get(entryId));
    }

    @GetMapping("/entries/{entryId}/summary")
    public AttendanceSummaryResponse summary(
            @PathVariable UUID entryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant now
    ) {
        return AttendanceSummaryResponse.from(attendanceApplicationService.summary(entryId, now == null ? Instant.now() : now));
    }

    @GetMapping("/workers/{workerId}/entries")
    public List<AttendanceEntryResponse> workerEntries(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId
    ) {
        return attendanceApplicationService.workerEntries(tenantId, workerId).stream()
                .map(AttendanceEntryResponse::from)
                .toList();
    }

    @GetMapping("/entries")
    public List<AttendanceEntryResponse> range(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return attendanceApplicationService.range(tenantId, from, to).stream()
                .map(AttendanceEntryResponse::from)
                .toList();
    }
}
