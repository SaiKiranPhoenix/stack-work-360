package com.stackwork360.compensationservice.application;

import com.stackwork360.compensationservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CompensationApplicationService {
    private final SalaryBandRepository salaryBandRepository;
    private final CompensationHistoryRepository historyRepository;
    private final CompensationChangeRequestRepository requestRepository;
    private final CompensationBenchmarkRepository benchmarkRepository;

    public CompensationApplicationService(
            SalaryBandRepository salaryBandRepository,
            CompensationHistoryRepository historyRepository,
            CompensationChangeRequestRepository requestRepository,
            CompensationBenchmarkRepository benchmarkRepository
    ) {
        this.salaryBandRepository = salaryBandRepository;
        this.historyRepository = historyRepository;
        this.requestRepository = requestRepository;
        this.benchmarkRepository = benchmarkRepository;
    }

    public SalaryBand createSalaryBand(CreateSalaryBandCommand command) {
        return salaryBandRepository.save(new SalaryBand(null, command.tenantId(), command.jobLevel(), command.location(), command.minimum(), command.midpoint(), command.maximum()));
    }

    public CompensationHistory recordCompensation(RecordCompensationCommand command) {
        return historyRepository.save(new CompensationHistory(null, command.tenantId(), command.workerId(), command.jobLevel(), command.location(), command.salary(), command.effectiveDate(), command.source(), null));
    }

    public CompensationChangeRequest requestChange(RequestCompensationChangeCommand command) {
        return requestRepository.save(CompensationChangeRequest.request(
                command.tenantId(),
                command.workerId(),
                command.currentJobLevel(),
                command.proposedJobLevel(),
                command.currentSalary(),
                command.proposedSalary(),
                command.effectiveDate(),
                command.reason()
        ));
    }

    public CompensationChangeRequest approve(UUID requestId, String actorId) {
        CompensationChangeRequest request = request(requestId);
        request.approve(actorId);
        return requestRepository.save(request);
    }

    public CompensationChangeRequest reject(UUID requestId, String actorId) {
        CompensationChangeRequest request = request(requestId);
        request.reject(actorId);
        return requestRepository.save(request);
    }

    public CompensationChangeRequest apply(UUID requestId) {
        CompensationChangeRequest request = request(requestId);
        request.apply();
        historyRepository.save(new CompensationHistory(null, request.tenantId(), request.workerId(), request.proposedJobLevel(), "current", request.proposedSalary(), request.effectiveDate(), "change-request:" + request.id(), null));
        return requestRepository.save(request);
    }

    public CompensationBenchmark importBenchmark(ImportBenchmarkCommand command) {
        return benchmarkRepository.save(new CompensationBenchmark(null, command.tenantId(), command.jobLevel(), command.location(), command.marketMedian(), command.source(), null));
    }

    public List<PayEquityAlert> payEquityAlerts(String tenantId) {
        return historyRepository.findByTenantId(tenantId).stream()
                .flatMap(history -> alertFor(history).stream())
                .toList();
    }

    public BudgetImpactReport budgetImpact(String tenantId) {
        List<CompensationChangeRequest> approved = requestRepository.findByTenantId(tenantId).stream()
                .filter(request -> request.status() == CompensationChangeStatus.APPROVED || request.status() == CompensationChangeStatus.APPLIED)
                .toList();
        Money total = approved.stream()
                .map(CompensationChangeRequest::budgetImpact)
                .reduce((left, right) -> left.plus(right))
                .orElseGet(() -> Money.of("0", "USD"));
        return new BudgetImpactReport(tenantId, approved.size(), total, approved.stream().map(request -> request.id().toString()).toList());
    }

    public SalaryBand salaryBand(UUID id) {
        return salaryBandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("salary band not found"));
    }

    public CompensationChangeRequest request(UUID id) {
        return requestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("compensation request not found"));
    }

    public List<SalaryBand> salaryBands(String tenantId) {
        return salaryBandRepository.findByTenantId(tenantId);
    }

    public List<CompensationChangeRequest> requests(String tenantId) {
        return requestRepository.findByTenantId(tenantId);
    }

    public List<CompensationHistory> history(String tenantId, String workerId) {
        return workerId == null ? historyRepository.findByTenantId(tenantId) : historyRepository.findByWorker(tenantId, workerId);
    }

    private List<PayEquityAlert> alertFor(CompensationHistory history) {
        return salaryBandRepository.find(history.tenantId(), history.jobLevel(), history.location())
                .map(band -> {
                    CompensationBenchmark benchmark = benchmarkRepository.find(history.tenantId(), history.jobLevel(), history.location()).orElse(null);
                    Money median = benchmark == null ? band.midpoint() : benchmark.marketMedian();
                    if (history.salary().compareTo(band.minimum()) < 0) {
                        return List.of(PayEquityAlert.belowBand(history.workerId(), history.salary(), band, median));
                    }
                    if (history.salary().compareTo(band.maximum()) > 0) {
                        return List.of(PayEquityAlert.aboveBand(history.workerId(), history.salary(), band, median));
                    }
                    BigDecimal threshold = median.amount().multiply(new BigDecimal("0.90"));
                    if (history.salary().amount().compareTo(threshold) < 0) {
                        return List.of(PayEquityAlert.belowMarket(history.workerId(), history.salary(), band, median));
                    }
                    return List.<PayEquityAlert>of();
                })
                .orElse(List.of());
    }
}
