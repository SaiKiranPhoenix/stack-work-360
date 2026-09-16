package com.stackwork360.speakupcaseservice.infrastructure;

import com.stackwork360.speakupcaseservice.domain.SpeakUpCase;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemorySpeakUpCaseRepository implements SpeakUpCaseRepository {
    private final ConcurrentMap<UUID, SpeakUpCase> casesById = new ConcurrentHashMap<>();

    @Override
    public SpeakUpCase save(SpeakUpCase speakUpCase) {
        casesById.put(speakUpCase.id(), speakUpCase);
        return speakUpCase;
    }

    @Override
    public Optional<SpeakUpCase> findById(UUID id) {
        return Optional.ofNullable(casesById.get(id));
    }

    @Override
    public List<SpeakUpCase> findByTenantId(String tenantId) {
        return casesById.values().stream()
                .filter(speakUpCase -> speakUpCase.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(SpeakUpCase::updatedAt).reversed())
                .toList();
    }
}
