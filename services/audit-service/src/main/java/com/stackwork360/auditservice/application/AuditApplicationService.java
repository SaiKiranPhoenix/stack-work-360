package com.stackwork360.auditservice.application;

import com.stackwork360.auditservice.domain.AuditAction;
import com.stackwork360.auditservice.domain.AuditOutcome;
import com.stackwork360.auditservice.domain.AuditQuery;
import com.stackwork360.auditservice.domain.AuditRecord;
import com.stackwork360.auditservice.domain.AuditRecordRepository;
import com.stackwork360.auditservice.domain.AuditRetentionPolicy;
import com.stackwork360.auditservice.domain.AuditRetentionPolicyRepository;
import com.stackwork360.auditservice.domain.AuditSensitivity;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuditApplicationService {
    private final AuditRecordRepository auditRecordRepository;
    private final AuditRetentionPolicyRepository retentionPolicyRepository;

    public AuditApplicationService(
            AuditRecordRepository auditRecordRepository,
            AuditRetentionPolicyRepository retentionPolicyRepository
    ) {
        this.auditRecordRepository = auditRecordRepository;
        this.retentionPolicyRepository = retentionPolicyRepository;
    }

    public AuditRecord record(RecordAuditCommand command) {
        return auditRecordRepository.append(AuditRecord.record(
                command.tenantId(),
                command.actorId(),
                command.serviceName(),
                command.resourceType(),
                command.resourceId(),
                command.action(),
                command.outcome(),
                command.sensitivity(),
                command.reason(),
                command.correlationId(),
                command.metadata()
        ));
    }

    public AuditRecord recordSensitiveRead(SensitiveReadCommand command) {
        return record(new RecordAuditCommand(
                command.tenantId(),
                command.actorId(),
                command.serviceName(),
                command.resourceType(),
                command.resourceId(),
                AuditAction.SENSITIVE_READ,
                AuditOutcome.SUCCESS,
                AuditSensitivity.RESTRICTED,
                command.reason(),
                command.correlationId(),
                command.metadata()
        ));
    }

    public AuditRecord get(UUID auditRecordId) {
        return auditRecordRepository.findById(auditRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("audit record not found"));
    }

    public List<AuditRecord> search(AuditQuery query) {
        return auditRecordRepository.search(query);
    }

    public AuditEvidenceExport exportEvidence(AuditQuery query) {
        return AuditEvidenceExport.create(query.tenantId(), search(query));
    }

    public AuditRetentionPolicy retentionPolicy(String tenantId) {
        return retentionPolicyRepository.findByTenantId(tenantId)
                .orElseGet(() -> retentionPolicyRepository.save(AuditRetentionPolicy.defaults(tenantId)));
    }

    public AuditRetentionPolicy updateRetentionPolicy(UpdateRetentionPolicyCommand command) {
        AuditRetentionPolicy policy = retentionPolicy(command.tenantId());
        policy.update(command.standardRetentionDays(), command.restrictedRetentionDays(), command.legalHoldEnabled());
        return retentionPolicyRepository.save(policy);
    }
}
