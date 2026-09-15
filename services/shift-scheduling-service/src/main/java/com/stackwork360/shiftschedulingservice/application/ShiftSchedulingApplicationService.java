package com.stackwork360.shiftschedulingservice.application;

import com.stackwork360.shiftschedulingservice.domain.RotatingSchedule;
import com.stackwork360.shiftschedulingservice.domain.ShiftAssignment;
import com.stackwork360.shiftschedulingservice.domain.ShiftAssignmentRepository;
import com.stackwork360.shiftschedulingservice.domain.ShiftSwapRequest;
import com.stackwork360.shiftschedulingservice.domain.ShiftSwapRequestRepository;
import com.stackwork360.shiftschedulingservice.domain.ShiftTemplate;
import com.stackwork360.shiftschedulingservice.domain.ShiftTemplateRepository;
import com.stackwork360.shiftschedulingservice.domain.StaffingCoverage;
import com.stackwork360.web.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ShiftSchedulingApplicationService {
    private final ShiftTemplateRepository templateRepository;
    private final ShiftAssignmentRepository assignmentRepository;
    private final ShiftSwapRequestRepository swapRequestRepository;

    public ShiftSchedulingApplicationService(
            ShiftTemplateRepository templateRepository,
            ShiftAssignmentRepository assignmentRepository,
            ShiftSwapRequestRepository swapRequestRepository
    ) {
        this.templateRepository = templateRepository;
        this.assignmentRepository = assignmentRepository;
        this.swapRequestRepository = swapRequestRepository;
    }

    public ShiftTemplate createTemplate(CreateShiftTemplateCommand command) {
        return templateRepository.save(new ShiftTemplate(
                null,
                command.tenantId(),
                command.name(),
                command.startTime(),
                command.endTime(),
                command.requiredStaff()
        ));
    }

    public ShiftAssignment assign(AssignShiftCommand command) {
        ShiftTemplate template = template(command.templateId());
        ShiftAssignment assignment = ShiftAssignment.assign(template.tenantId(), template, command.workerId(), command.shiftDate());
        ensureNoConflict(assignment);
        return assignmentRepository.save(assignment);
    }

    public List<ShiftAssignment> generateRotatingAssignments(CreateRotatingScheduleCommand command, LocalDate throughDate) {
        ShiftTemplate template = template(command.templateId());
        RotatingSchedule schedule = new RotatingSchedule(null, command.tenantId(), command.templateId(), command.workerRotation(), command.startsOn());
        return java.util.stream.Stream.iterate(command.startsOn(), date -> !date.isAfter(throughDate), date -> date.plusDays(1))
                .map(date -> assign(new AssignShiftCommand(template.id(), schedule.workerFor(date), date)))
                .toList();
    }

    public ShiftSwapRequest requestSwap(RequestShiftSwapCommand command) {
        ShiftAssignment assignment = assignment(command.assignmentId());
        if (!assignment.workerId().equals(command.requestedBy())) {
            throw new IllegalArgumentException("only assigned worker can request a swap");
        }
        return swapRequestRepository.save(ShiftSwapRequest.request(
                command.tenantId(),
                command.assignmentId(),
                command.requestedBy(),
                command.targetWorkerId(),
                command.reason()
        ));
    }

    public ShiftSwapRequest approveSwap(UUID swapRequestId, String actorId) {
        ShiftSwapRequest request = swapRequest(swapRequestId);
        ShiftAssignment assignment = assignment(request.assignmentId());
        ShiftAssignment candidate = ShiftAssignment.assign(
                assignment.tenantId(),
                template(assignment.templateId()),
                request.targetWorkerId(),
                assignment.shiftDate()
        );
        ensureNoConflictExcept(candidate, assignment.id());
        request.approve(actorId);
        assignment.reassignTo(request.targetWorkerId());
        assignmentRepository.save(assignment);
        return swapRequestRepository.save(request);
    }

    public ShiftSwapRequest rejectSwap(UUID swapRequestId, String actorId) {
        ShiftSwapRequest request = swapRequest(swapRequestId);
        request.reject(actorId);
        return swapRequestRepository.save(request);
    }

    public StaffingCoverage coverage(UUID templateId, LocalDate shiftDate) {
        ShiftTemplate template = template(templateId);
        long assigned = assignmentRepository.findByTenantIdAndDate(template.tenantId(), shiftDate).stream()
                .filter(assignment -> assignment.templateId().equals(templateId))
                .count();
        return new StaffingCoverage(template.id(), shiftDate, template.requiredStaff(), (int) assigned);
    }

    public ShiftTemplate template(UUID templateId) {
        return templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("shift template not found"));
    }

    public ShiftAssignment assignment(UUID assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("shift assignment not found"));
    }

    public ShiftSwapRequest swapRequest(UUID swapRequestId) {
        return swapRequestRepository.findById(swapRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("shift swap request not found"));
    }

    public List<ShiftTemplate> templates(String tenantId) {
        return templateRepository.findByTenantId(tenantId);
    }

    public List<ShiftAssignment> assignments(String tenantId, LocalDate date) {
        return assignmentRepository.findByTenantIdAndDate(tenantId, date);
    }

    public List<ShiftSwapRequest> swaps(String tenantId) {
        return swapRequestRepository.findByTenantId(tenantId);
    }

    private void ensureNoConflict(ShiftAssignment candidate) {
        ensureNoConflictExcept(candidate, null);
    }

    private void ensureNoConflictExcept(ShiftAssignment candidate, UUID ignoredAssignmentId) {
        boolean conflict = assignmentRepository.findByTenantIdAndWorkerId(candidate.tenantId(), candidate.workerId()).stream()
                .filter(existing -> ignoredAssignmentId == null || !existing.id().equals(ignoredAssignmentId))
                .anyMatch(existing -> existing.conflictsWith(candidate));
        if (conflict) {
            throw new IllegalArgumentException("shift assignment conflicts with an existing assignment");
        }
    }
}
