package com.stackwork360.peoplecoreservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.peoplecoreservice.application.CreateWorkerCommand;
import com.stackwork360.peoplecoreservice.application.PeopleCoreApplicationService;
import com.stackwork360.peoplecoreservice.application.UpdateWorkerCommand;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/people-core/v1/workers")
public class PeopleCoreController {
    private final PeopleCoreApplicationService peopleCoreApplicationService;

    public PeopleCoreController(PeopleCoreApplicationService peopleCoreApplicationService) {
        this.peopleCoreApplicationService = peopleCoreApplicationService;
    }

    @PostMapping
    public ResponseEntity<WorkerResponse> create(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateWorkerRequest request
    ) {
        WorkerResponse response = WorkerResponse.from(peopleCoreApplicationService.create(new CreateWorkerCommand(
                tenantId,
                request.employeeNumber(),
                request.firstName(),
                request.lastName(),
                request.workEmail(),
                request.employmentType(),
                request.jobTitle(),
                request.departmentId(),
                request.managerWorkerId(),
                request.startDate()
        )));

        return ResponseEntity.created(URI.create("/api/people-core/v1/workers/" + response.id()))
                .body(response);
    }

    @GetMapping
    public List<WorkerResponse> list(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return peopleCoreApplicationService.listByTenant(tenantId).stream()
                .map(WorkerResponse::from)
                .toList();
    }

    @GetMapping("/{workerId}")
    public WorkerResponse get(@PathVariable UUID workerId) {
        return WorkerResponse.from(peopleCoreApplicationService.get(workerId));
    }

    @PutMapping("/{workerId}")
    public WorkerResponse update(
            @PathVariable UUID workerId,
            @Valid @RequestBody UpdateWorkerRequest request
    ) {
        return WorkerResponse.from(peopleCoreApplicationService.update(workerId, new UpdateWorkerCommand(
                request.firstName(),
                request.lastName(),
                request.workEmail(),
                request.jobTitle(),
                request.departmentId(),
                request.managerWorkerId()
        )));
    }

    @PatchMapping("/{workerId}/activate")
    public WorkerResponse activate(@PathVariable UUID workerId) {
        return WorkerResponse.from(peopleCoreApplicationService.activate(workerId));
    }

    @PatchMapping("/{workerId}/offboarding")
    public WorkerResponse startOffboarding(
            @PathVariable UUID workerId,
            @Valid @RequestBody StartOffboardingRequest request
    ) {
        return WorkerResponse.from(peopleCoreApplicationService.startOffboarding(workerId, request.endDate()));
    }

    @PatchMapping("/{workerId}/alumni")
    public WorkerResponse markAlumni(@PathVariable UUID workerId) {
        return WorkerResponse.from(peopleCoreApplicationService.markAlumni(workerId));
    }
}
