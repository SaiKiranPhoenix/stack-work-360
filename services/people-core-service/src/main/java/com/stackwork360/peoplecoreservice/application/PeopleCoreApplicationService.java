package com.stackwork360.peoplecoreservice.application;

import com.stackwork360.peoplecoreservice.domain.Worker;
import com.stackwork360.peoplecoreservice.domain.WorkerRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PeopleCoreApplicationService {
    private final WorkerRepository workerRepository;

    public PeopleCoreApplicationService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public Worker create(CreateWorkerCommand command) {
        if (workerRepository.existsByTenantIdAndEmployeeNumber(command.tenantId(), command.employeeNumber())) {
            throw new IllegalArgumentException("employee number is already in use for tenant");
        }
        if (workerRepository.existsByTenantIdAndWorkEmail(command.tenantId(), command.workEmail())) {
            throw new IllegalArgumentException("work email is already in use for tenant");
        }

        Worker worker = Worker.hire(
                command.tenantId(),
                command.employeeNumber(),
                command.firstName(),
                command.lastName(),
                command.workEmail(),
                command.employmentType(),
                command.jobTitle(),
                command.departmentId(),
                command.managerWorkerId(),
                command.startDate()
        );
        return workerRepository.save(worker);
    }

    public Worker update(UUID workerId, UpdateWorkerCommand command) {
        Worker worker = get(workerId);
        worker.updateProfile(
                command.firstName(),
                command.lastName(),
                command.workEmail(),
                command.jobTitle(),
                command.departmentId(),
                command.managerWorkerId()
        );
        return workerRepository.save(worker);
    }

    public Worker activate(UUID workerId) {
        Worker worker = get(workerId);
        worker.markActive();
        return workerRepository.save(worker);
    }

    public Worker startOffboarding(UUID workerId, LocalDate endDate) {
        Worker worker = get(workerId);
        worker.startOffboarding(endDate);
        return workerRepository.save(worker);
    }

    public Worker markAlumni(UUID workerId) {
        Worker worker = get(workerId);
        worker.markAlumni();
        return workerRepository.save(worker);
    }

    public Worker get(UUID workerId) {
        return workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("worker not found"));
    }

    public List<Worker> listByTenant(String tenantId) {
        return workerRepository.findByTenantId(tenantId);
    }
}
