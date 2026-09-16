package com.stackwork360.learningservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.learningservice.domain.*;
import com.stackwork360.learningservice.infrastructure.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class LearningApplicationServiceTest {
    private final LearningApplicationService service = new LearningApplicationService(
            new InMemoryLearningResourceRepository(),
            new InMemoryLearningPathRepository(),
            new InMemoryCertificationRepository(),
            new InMemoryCourseCompletionRepository(),
            new InMemoryManagerLearningAssignmentRepository(),
            new InMemoryRecommendationInputRepository()
    );

    @Test
    void completesManagerAssignmentWhenCourseCompletionIsRecorded() {
        LearningResource resource = service.createResource(resourceCommand("Kafka Basics", "Kafka"));
        service.assign(new AssignLearningCommand("tenant-1", "worker-1", "manager-1", resource.id(), LocalDate.now().plusDays(14)));

        service.recordCompletion(new RecordCompletionCommand("tenant-1", "worker-1", resource.id(), new BigDecimal("88")));

        assertThat(service.assignments("tenant-1", "worker-1"))
                .singleElement()
                .satisfies(assignment -> assertThat(assignment.status()).isEqualTo(AssignmentStatus.COMPLETED));
    }

    @Test
    void recommendsActiveResourcesMatchingSkillGapsExcludingCompletedResources() {
        LearningResource kafka = service.createResource(resourceCommand("Kafka Basics", "Kafka"));
        LearningResource spring = service.createResource(resourceCommand("Spring Boot Advanced", "Spring Boot"));
        service.recordCompletion(new RecordCompletionCommand("tenant-1", "worker-1", kafka.id(), new BigDecimal("90")));
        service.captureRecommendationInput(new CaptureRecommendationInputCommand("tenant-1", "worker-1", "Senior Backend Engineer", List.of("Kafka", "Spring Boot")));

        LearningRecommendation recommendation = service.recommendations("tenant-1", "worker-1");

        assertThat(recommendation.resourceIds()).containsExactly(spring.id());
    }

    @Test
    void createsLearningPathAndCertification() {
        LearningResource resource = service.createResource(resourceCommand("Security Fundamentals", "Security"));

        LearningPath path = service.createPath(new CreateLearningPathCommand("tenant-1", "Backend Security", "Backend Engineer", List.of(resource.id()), true));
        Certification certification = service.createCertification(new CreateCertificationCommand("tenant-1", "Security Ready", "StackWork", List.of(resource.id()), 24, LocalDate.now()));

        assertThat(path.resourceIds()).containsExactly(resource.id());
        assertThat(certification.requiredResourceIds()).containsExactly(resource.id());
    }

    private CreateLearningResourceCommand resourceCommand(String title, String skill) {
        return new CreateLearningResourceCommand("tenant-1", title, LearningResourceType.COURSE, "internal", 60, List.of(skill), true);
    }
}
