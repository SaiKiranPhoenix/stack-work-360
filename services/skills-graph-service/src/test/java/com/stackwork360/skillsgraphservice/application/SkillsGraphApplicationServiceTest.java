package com.stackwork360.skillsgraphservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.skillsgraphservice.domain.*;
import com.stackwork360.skillsgraphservice.infrastructure.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class SkillsGraphApplicationServiceTest {
    private final SkillsGraphApplicationService service = new SkillsGraphApplicationService(
            new InMemorySkillRepository(),
            new InMemoryWorkerSkillProfileRepository(),
            new InMemorySkillEvidenceRepository(),
            new InMemorySkillEndorsementRepository(),
            new InMemoryCertificationSkillMappingRepository(),
            new InMemoryProjectSkillMappingRepository()
    );

    @Test
    void recordsEvidenceAndUpdatesWorkerProfile() {
        seedSkills();

        service.recordEvidence(new RecordEvidenceCommand("tenant-1", "worker-1", "JAVA", EvidenceType.PROJECT, "project-1", ProficiencyLevel.ADVANCED));

        WorkerSkillProfile profile = service.profiles("tenant-1").get(0);
        assertThat(profile.hasSkill("JAVA", ProficiencyLevel.PROFICIENT)).isTrue();
        assertThat(service.evidence("tenant-1", "worker-1")).hasSize(1);
    }

    @Test
    void reportsOnlyMissingSkillGaps() {
        seedSkills();
        service.upsertWorkerSkill(new UpsertWorkerSkillCommand("tenant-1", "worker-1", "JAVA", ProficiencyLevel.ADVANCED));

        SkillGapReport report = service.skillGap("tenant-1", "worker-1", List.of(
                new RequiredSkill("JAVA", ProficiencyLevel.PROFICIENT),
                new RequiredSkill("KAFKA", ProficiencyLevel.WORKING)
        ));

        assertThat(report.gaps()).singleElement()
                .satisfies(gap -> assertThat(gap.skillCode()).isEqualTo("KAFKA"));
    }

    @Test
    void calculatesSuccessionCoverageAndReadyWorkers() {
        seedSkills();
        service.upsertWorkerSkill(new UpsertWorkerSkillCommand("tenant-1", "worker-1", "JAVA", ProficiencyLevel.EXPERT));
        service.upsertWorkerSkill(new UpsertWorkerSkillCommand("tenant-1", "worker-1", "KAFKA", ProficiencyLevel.PROFICIENT));
        service.upsertWorkerSkill(new UpsertWorkerSkillCommand("tenant-1", "worker-2", "JAVA", ProficiencyLevel.PROFICIENT));

        SuccessionSkillCoverage coverage = service.successionCoverage("tenant-1", "Staff Engineer", List.of(
                new RequiredSkill("JAVA", ProficiencyLevel.ADVANCED),
                new RequiredSkill("KAFKA", ProficiencyLevel.WORKING)
        ));

        assertThat(coverage.candidateCount()).isEqualTo(2);
        assertThat(coverage.averageCoverage()).isEqualByComparingTo("75.00");
        assertThat(coverage.readyWorkerIds()).containsExactly("worker-1");
    }

    @Test
    void createsCertificationAndProjectMappings() {
        seedSkills();

        CertificationSkillMapping certification = service.createCertificationMapping(new CreateCertificationMappingCommand("tenant-1", "CERT-JAVA", List.of("JAVA")));
        ProjectSkillMapping project = service.createProjectMapping(new CreateProjectMappingCommand("tenant-1", "PAYROLL", List.of("JAVA", "KAFKA")));

        assertThat(certification.skillCodes()).containsExactly("JAVA");
        assertThat(project.skillCodes()).containsExactly("JAVA", "KAFKA");
    }

    private void seedSkills() {
        service.createSkill(new CreateSkillCommand("tenant-1", "JAVA", "Java", SkillCategory.TECHNICAL, null, true));
        service.createSkill(new CreateSkillCommand("tenant-1", "KAFKA", "Kafka", SkillCategory.TECHNICAL, null, true));
    }
}
