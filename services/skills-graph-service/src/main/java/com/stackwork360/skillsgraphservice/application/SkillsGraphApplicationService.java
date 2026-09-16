package com.stackwork360.skillsgraphservice.application;

import com.stackwork360.skillsgraphservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SkillsGraphApplicationService {
    private final SkillRepository skillRepository;
    private final WorkerSkillProfileRepository profileRepository;
    private final SkillEvidenceRepository evidenceRepository;
    private final SkillEndorsementRepository endorsementRepository;
    private final CertificationSkillMappingRepository certificationMappingRepository;
    private final ProjectSkillMappingRepository projectMappingRepository;

    public SkillsGraphApplicationService(
            SkillRepository skillRepository,
            WorkerSkillProfileRepository profileRepository,
            SkillEvidenceRepository evidenceRepository,
            SkillEndorsementRepository endorsementRepository,
            CertificationSkillMappingRepository certificationMappingRepository,
            ProjectSkillMappingRepository projectMappingRepository
    ) {
        this.skillRepository = skillRepository;
        this.profileRepository = profileRepository;
        this.evidenceRepository = evidenceRepository;
        this.endorsementRepository = endorsementRepository;
        this.certificationMappingRepository = certificationMappingRepository;
        this.projectMappingRepository = projectMappingRepository;
    }

    public Skill createSkill(CreateSkillCommand command) {
        return skillRepository.save(new Skill(null, command.tenantId(), command.code(), command.name(), command.category(), command.parentSkillCode(), command.active()));
    }

    public WorkerSkillProfile upsertWorkerSkill(UpsertWorkerSkillCommand command) {
        ensureSkill(command.tenantId(), command.skillCode());
        WorkerSkillProfile profile = profileRepository.findByWorker(command.tenantId(), command.workerId())
                .orElseGet(() -> new WorkerSkillProfile(null, command.tenantId(), command.workerId(), List.of(), null));
        profile.upsertSkill(command.skillCode(), command.level());
        return profileRepository.save(profile);
    }

    public SkillEvidence recordEvidence(RecordEvidenceCommand command) {
        ensureSkill(command.tenantId(), command.skillCode());
        SkillEvidence evidence = evidenceRepository.save(new SkillEvidence(null, command.tenantId(), command.workerId(), command.skillCode(), command.type(), command.sourceId(), command.level(), null));
        upsertWorkerSkill(new UpsertWorkerSkillCommand(command.tenantId(), command.workerId(), command.skillCode(), command.level()));
        return evidence;
    }

    public SkillEndorsement endorse(EndorseSkillCommand command) {
        ensureSkill(command.tenantId(), command.skillCode());
        SkillEndorsement endorsement = endorsementRepository.save(new SkillEndorsement(null, command.tenantId(), command.workerId(), command.skillCode(), command.endorsedBy(), command.level(), command.note(), null));
        upsertWorkerSkill(new UpsertWorkerSkillCommand(command.tenantId(), command.workerId(), command.skillCode(), command.level()));
        return endorsement;
    }

    public CertificationSkillMapping createCertificationMapping(CreateCertificationMappingCommand command) {
        command.skillCodes().forEach(skillCode -> ensureSkill(command.tenantId(), skillCode));
        return certificationMappingRepository.save(new CertificationSkillMapping(null, command.tenantId(), command.certificationCode(), command.skillCodes()));
    }

    public ProjectSkillMapping createProjectMapping(CreateProjectMappingCommand command) {
        command.skillCodes().forEach(skillCode -> ensureSkill(command.tenantId(), skillCode));
        return projectMappingRepository.save(new ProjectSkillMapping(null, command.tenantId(), command.projectCode(), command.skillCodes()));
    }

    public SkillGapReport skillGap(String tenantId, String workerId, List<RequiredSkill> requiredSkills) {
        WorkerSkillProfile profile = profileRepository.findByWorker(tenantId, workerId)
                .orElseGet(() -> new WorkerSkillProfile(null, tenantId, workerId, List.of(), null));
        List<SkillGap> gaps = requiredSkills.stream()
                .map(required -> {
                    ProficiencyLevel current = profile.skills().stream()
                            .filter(skill -> skill.skillCode().equals(required.skillCode()))
                            .map(WorkerSkill::level)
                            .findFirst()
                            .orElse(null);
                    boolean missing = current == null || !current.atLeast(required.requiredLevel());
                    return new SkillGap(required.skillCode(), required.requiredLevel(), current, missing);
                })
                .filter(SkillGap::missing)
                .toList();
        return new SkillGapReport(tenantId, workerId, gaps);
    }

    public SuccessionSkillCoverage successionCoverage(String tenantId, String roleName, List<RequiredSkill> requiredSkills) {
        List<WorkerSkillProfile> profiles = profileRepository.findByTenantId(tenantId);
        List<String> ready = profiles.stream()
                .filter(profile -> requiredSkills.stream().allMatch(required -> profile.hasSkill(required.skillCode(), required.requiredLevel())))
                .map(WorkerSkillProfile::workerId)
                .sorted()
                .toList();
        BigDecimal average = profiles.isEmpty()
                ? BigDecimal.ZERO.setScale(2)
                : profiles.stream()
                        .map(profile -> coveragePercent(profile, requiredSkills))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(profiles.size()), 2, RoundingMode.HALF_UP);
        return new SuccessionSkillCoverage(tenantId, roleName, profiles.size(), average, ready);
    }

    public List<Skill> skills(String tenantId) {
        return skillRepository.findByTenantId(tenantId);
    }

    public List<WorkerSkillProfile> profiles(String tenantId) {
        return profileRepository.findByTenantId(tenantId);
    }

    public List<SkillEvidence> evidence(String tenantId, String workerId) {
        return evidenceRepository.findByWorker(tenantId, workerId);
    }

    public List<SkillEndorsement> endorsements(String tenantId, String workerId) {
        return endorsementRepository.findByWorker(tenantId, workerId);
    }

    public List<CertificationSkillMapping> certificationMappings(String tenantId) {
        return certificationMappingRepository.findByTenantId(tenantId);
    }

    public List<ProjectSkillMapping> projectMappings(String tenantId) {
        return projectMappingRepository.findByTenantId(tenantId);
    }

    private BigDecimal coveragePercent(WorkerSkillProfile profile, List<RequiredSkill> requiredSkills) {
        if (requiredSkills.isEmpty()) {
            return BigDecimal.ZERO.setScale(2);
        }
        long matched = requiredSkills.stream()
                .filter(required -> profile.hasSkill(required.skillCode(), required.requiredLevel()))
                .count();
        return BigDecimal.valueOf(matched)
                .multiply(new BigDecimal("100"))
                .divide(BigDecimal.valueOf(requiredSkills.size()), 2, RoundingMode.HALF_UP);
    }

    private void ensureSkill(String tenantId, String skillCode) {
        skillRepository.findByCode(tenantId, skillCode)
                .orElseThrow(() -> new ResourceNotFoundException("skill not found"));
    }
}
