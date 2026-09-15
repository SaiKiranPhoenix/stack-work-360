package com.stackwork360.auditservice.api;

import com.stackwork360.auditservice.application.AuditApplicationService;
import com.stackwork360.auditservice.domain.AuditAction;
import com.stackwork360.auditservice.domain.AuditQuery;
import com.stackwork360.auditservice.domain.AuditSensitivity;
import com.stackwork360.common.CorrelationIds;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit/v1")
public class AuditController {
    private final AuditApplicationService auditApplicationService;

    public AuditController(AuditApplicationService auditApplicationService) {
        this.auditApplicationService = auditApplicationService;
    }

    @PostMapping("/records")
    public ResponseEntity<AuditRecordResponse> record(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RecordAuditRequest request
    ) {
        AuditRecordResponse response = AuditRecordResponse.from(auditApplicationService.record(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/audit/v1/records/" + response.id()))
                .body(response);
    }

    @PostMapping("/sensitive-reads")
    public ResponseEntity<AuditRecordResponse> sensitiveRead(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody SensitiveReadRequest request
    ) {
        AuditRecordResponse response = AuditRecordResponse.from(auditApplicationService.recordSensitiveRead(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/audit/v1/records/" + response.id()))
                .body(response);
    }

    @GetMapping("/records/{recordId}")
    public AuditRecordResponse get(@PathVariable UUID recordId) {
        return AuditRecordResponse.from(auditApplicationService.get(recordId));
    }

    @GetMapping("/records")
    public List<AuditRecordResponse> search(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) String actorId,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) String resourceType,
            @RequestParam(required = false) AuditAction action,
            @RequestParam(required = false) AuditSensitivity sensitivity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return auditApplicationService.search(query(tenantId, actorId, serviceName, resourceType, action, sensitivity, from, to)).stream()
                .map(AuditRecordResponse::from)
                .toList();
    }

    @GetMapping("/evidence-export")
    public AuditEvidenceExportResponse export(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) String actorId,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) String resourceType,
            @RequestParam(required = false) AuditAction action,
            @RequestParam(required = false) AuditSensitivity sensitivity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return AuditEvidenceExportResponse.from(auditApplicationService.exportEvidence(query(
                tenantId,
                actorId,
                serviceName,
                resourceType,
                action,
                sensitivity,
                from,
                to
        )));
    }

    @GetMapping("/retention-policy")
    public AuditRetentionPolicyResponse retentionPolicy(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return AuditRetentionPolicyResponse.from(auditApplicationService.retentionPolicy(tenantId));
    }

    @PutMapping("/retention-policy")
    public AuditRetentionPolicyResponse updateRetentionPolicy(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody UpdateRetentionPolicyRequest request
    ) {
        return AuditRetentionPolicyResponse.from(auditApplicationService.updateRetentionPolicy(request.toCommand(tenantId)));
    }

    private static AuditQuery query(
            String tenantId,
            String actorId,
            String serviceName,
            String resourceType,
            AuditAction action,
            AuditSensitivity sensitivity,
            Instant from,
            Instant to
    ) {
        return new AuditQuery(tenantId, actorId, serviceName, resourceType, action, sensitivity, from, to);
    }
}
