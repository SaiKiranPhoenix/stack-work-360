package com.stackwork360.peoplecoreservice.infrastructure;

import com.stackwork360.peoplecoreservice.domain.Worker;
import com.stackwork360.peoplecoreservice.domain.WorkerRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryWorkerRepository implements WorkerRepository {
    private final ConcurrentMap<UUID, Worker> workersById = new ConcurrentHashMap<>();

    @Override
    public Worker save(Worker worker) {
        workersById.put(worker.id(), worker);
        return worker;
    }

    @Override
    public Optional<Worker> findById(UUID id) {
        return Optional.ofNullable(workersById.get(id));
    }

    @Override
    public Optional<Worker> findByTenantIdAndEmployeeNumber(String tenantId, String employeeNumber) {
        return workersById.values().stream()
                .filter(worker -> worker.tenantId().equals(tenantId))
                .filter(worker -> worker.employeeNumber().equalsIgnoreCase(employeeNumber))
                .findFirst();
    }

    @Override
    public Optional<Worker> findByTenantIdAndWorkEmail(String tenantId, String workEmail) {
        String normalizedEmail = workEmail.toLowerCase(Locale.ROOT);
        return workersById.values().stream()
                .filter(worker -> worker.tenantId().equals(tenantId))
                .filter(worker -> worker.workEmail().equals(normalizedEmail))
                .findFirst();
    }

    @Override
    public List<Worker> findByTenantId(String tenantId) {
        return workersById.values().stream()
                .filter(worker -> worker.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(Worker::createdAt))
                .toList();
    }

    @Override
    public boolean existsByTenantIdAndEmployeeNumber(String tenantId, String employeeNumber) {
        return findByTenantIdAndEmployeeNumber(tenantId, employeeNumber).isPresent();
    }

    @Override
    public boolean existsByTenantIdAndWorkEmail(String tenantId, String workEmail) {
        return findByTenantIdAndWorkEmail(tenantId, workEmail).isPresent();
    }
}
