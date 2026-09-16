package com.stackwork360.benefitsservice.infrastructure;

import com.stackwork360.benefitsservice.domain.EmployeeEnrollment;
import com.stackwork360.benefitsservice.domain.EmployeeEnrollmentRepository;
import com.stackwork360.benefitsservice.domain.EnrollmentStatus;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEmployeeEnrollmentRepository implements EmployeeEnrollmentRepository {
    private final Map<UUID, EmployeeEnrollment> enrollments = new ConcurrentHashMap<>();

    public EmployeeEnrollment save(EmployeeEnrollment enrollment) {
        enrollments.put(enrollment.id(), enrollment);
        return enrollment;
    }

    public Optional<EmployeeEnrollment> findActive(String tenantId, String workerId, String planCode) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.tenantId().equals(tenantId))
                .filter(enrollment -> enrollment.workerId().equals(workerId))
                .filter(enrollment -> enrollment.planCode().equals(planCode))
                .filter(enrollment -> enrollment.status() == EnrollmentStatus.ACTIVE || enrollment.status() == EnrollmentStatus.WAIVED)
                .max(Comparator.comparing(EmployeeEnrollment::coverageStart));
    }

    public List<EmployeeEnrollment> findByTenantId(String tenantId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(EmployeeEnrollment::coverageStart).reversed())
                .toList();
    }

    public List<EmployeeEnrollment> findByWorker(String tenantId, String workerId) {
        return enrollments.values().stream()
                .filter(enrollment -> enrollment.tenantId().equals(tenantId))
                .filter(enrollment -> enrollment.workerId().equals(workerId))
                .sorted(Comparator.comparing(EmployeeEnrollment::coverageStart).reversed())
                .toList();
    }
}
