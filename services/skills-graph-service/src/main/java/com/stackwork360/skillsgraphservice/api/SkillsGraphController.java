package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.skillsgraphservice.application.SkillsGraphApplicationService;
import com.stackwork360.skillsgraphservice.domain.SkillGapReport;
import com.stackwork360.skillsgraphservice.domain.SuccessionSkillCoverage;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills-graph/v1")
public class SkillsGraphController {
    private final SkillsGraphApplicationService skillsGraphApplicationService;

    public SkillsGraphController(SkillsGraphApplicationService skillsGraphApplicationService) {
        this.skillsGraphApplicationService = skillsGraphApplicationService;
    }

    @PostMapping("/skills")
    public ResponseEntity<SkillResponse> createSkill(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateSkillRequest request) {
        SkillResponse response = SkillResponse.from(skillsGraphApplicationService.createSkill(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/skills-graph/v1/skills/" + response.code())).body(response);
    }

    @GetMapping("/skills")
    public List<SkillResponse> skills(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return skillsGraphApplicationService.skills(tenantId).stream().map(SkillResponse::from).toList();
    }

    @PostMapping("/profiles/skills")
    public WorkerSkillProfileResponse upsertWorkerSkill(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody UpsertWorkerSkillRequest request) {
        return WorkerSkillProfileResponse.from(skillsGraphApplicationService.upsertWorkerSkill(request.toCommand(tenantId)));
    }

    @GetMapping("/profiles")
    public List<WorkerSkillProfileResponse> profiles(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return skillsGraphApplicationService.profiles(tenantId).stream().map(WorkerSkillProfileResponse::from).toList();
    }

    @PostMapping("/evidence")
    public SkillEvidenceResponse recordEvidence(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody RecordEvidenceRequest request) {
        return SkillEvidenceResponse.from(skillsGraphApplicationService.recordEvidence(request.toCommand(tenantId)));
    }

    @GetMapping("/evidence")
    public List<SkillEvidenceResponse> evidence(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @RequestParam String workerId) {
        return skillsGraphApplicationService.evidence(tenantId, workerId).stream().map(SkillEvidenceResponse::from).toList();
    }

    @PostMapping("/endorsements")
    public SkillEndorsementResponse endorse(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody EndorseSkillRequest request) {
        return SkillEndorsementResponse.from(skillsGraphApplicationService.endorse(request.toCommand(tenantId)));
    }

    @GetMapping("/endorsements")
    public List<SkillEndorsementResponse> endorsements(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @RequestParam String workerId) {
        return skillsGraphApplicationService.endorsements(tenantId, workerId).stream().map(SkillEndorsementResponse::from).toList();
    }

    @PostMapping("/certification-mappings")
    public CertificationSkillMappingResponse createCertificationMapping(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateCertificationMappingRequest request) {
        return CertificationSkillMappingResponse.from(skillsGraphApplicationService.createCertificationMapping(request.toCommand(tenantId)));
    }

    @GetMapping("/certification-mappings")
    public List<CertificationSkillMappingResponse> certificationMappings(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return skillsGraphApplicationService.certificationMappings(tenantId).stream().map(CertificationSkillMappingResponse::from).toList();
    }

    @PostMapping("/project-mappings")
    public ProjectSkillMappingResponse createProjectMapping(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateProjectMappingRequest request) {
        return ProjectSkillMappingResponse.from(skillsGraphApplicationService.createProjectMapping(request.toCommand(tenantId)));
    }

    @GetMapping("/project-mappings")
    public List<ProjectSkillMappingResponse> projectMappings(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return skillsGraphApplicationService.projectMappings(tenantId).stream().map(ProjectSkillMappingResponse::from).toList();
    }

    @PostMapping("/skill-gaps")
    public SkillGapReport skillGap(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody SkillGapRequest request) {
        return skillsGraphApplicationService.skillGap(tenantId, request.workerId(), request.requiredSkills().stream().map(RequiredSkillRequest::toDomain).toList());
    }

    @PostMapping("/succession-coverage")
    public SuccessionSkillCoverage successionCoverage(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody SuccessionCoverageRequest request) {
        return skillsGraphApplicationService.successionCoverage(tenantId, request.roleName(), request.requiredSkills().stream().map(RequiredSkillRequest::toDomain).toList());
    }
}
