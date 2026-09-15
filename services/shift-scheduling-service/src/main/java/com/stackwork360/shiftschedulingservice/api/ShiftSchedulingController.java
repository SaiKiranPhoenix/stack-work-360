package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.shiftschedulingservice.application.ShiftSchedulingApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
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
@RequestMapping("/api/shift-scheduling/v1")
public class ShiftSchedulingController {
    private final ShiftSchedulingApplicationService shiftSchedulingApplicationService;

    public ShiftSchedulingController(ShiftSchedulingApplicationService shiftSchedulingApplicationService) {
        this.shiftSchedulingApplicationService = shiftSchedulingApplicationService;
    }

    @PostMapping("/templates")
    public ResponseEntity<ShiftTemplateResponse> createTemplate(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateShiftTemplateRequest request
    ) {
        ShiftTemplateResponse response = ShiftTemplateResponse.from(shiftSchedulingApplicationService.createTemplate(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/shift-scheduling/v1/templates/" + response.id()))
                .body(response);
    }

    @GetMapping("/templates")
    public List<ShiftTemplateResponse> templates(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return shiftSchedulingApplicationService.templates(tenantId).stream()
                .map(ShiftTemplateResponse::from)
                .toList();
    }

    @PostMapping("/assignments")
    public ResponseEntity<ShiftAssignmentResponse> assign(@Valid @RequestBody AssignShiftRequest request) {
        ShiftAssignmentResponse response = ShiftAssignmentResponse.from(shiftSchedulingApplicationService.assign(request.toCommand()));
        return ResponseEntity.created(URI.create("/api/shift-scheduling/v1/assignments/" + response.id()))
                .body(response);
    }

    @PostMapping("/rotations")
    public List<ShiftAssignmentResponse> generateRotation(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateRotatingScheduleRequest request
    ) {
        return shiftSchedulingApplicationService.generateRotatingAssignments(request.toCommand(tenantId), request.throughDate()).stream()
                .map(ShiftAssignmentResponse::from)
                .toList();
    }

    @GetMapping("/assignments")
    public List<ShiftAssignmentResponse> assignments(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return shiftSchedulingApplicationService.assignments(tenantId, date).stream()
                .map(ShiftAssignmentResponse::from)
                .toList();
    }

    @PostMapping("/swaps")
    public ResponseEntity<ShiftSwapRequestResponse> requestSwap(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RequestShiftSwapRequest request
    ) {
        ShiftSwapRequestResponse response = ShiftSwapRequestResponse.from(shiftSchedulingApplicationService.requestSwap(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/shift-scheduling/v1/swaps/" + response.id()))
                .body(response);
    }

    @PatchMapping("/swaps/{swapId}/approve")
    public ShiftSwapRequestResponse approveSwap(
            @PathVariable UUID swapId,
            @Valid @RequestBody ShiftDecisionRequest request
    ) {
        return ShiftSwapRequestResponse.from(shiftSchedulingApplicationService.approveSwap(swapId, request.actorId()));
    }

    @PatchMapping("/swaps/{swapId}/reject")
    public ShiftSwapRequestResponse rejectSwap(
            @PathVariable UUID swapId,
            @Valid @RequestBody ShiftDecisionRequest request
    ) {
        return ShiftSwapRequestResponse.from(shiftSchedulingApplicationService.rejectSwap(swapId, request.actorId()));
    }

    @GetMapping("/coverage")
    public StaffingCoverageResponse coverage(
            @RequestParam UUID templateId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return StaffingCoverageResponse.from(shiftSchedulingApplicationService.coverage(templateId, date));
    }
}
