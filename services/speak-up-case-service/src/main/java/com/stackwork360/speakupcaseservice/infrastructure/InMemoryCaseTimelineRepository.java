package com.stackwork360.speakupcaseservice.infrastructure;

import com.stackwork360.speakupcaseservice.domain.CaseTimelineEntry;
import com.stackwork360.speakupcaseservice.domain.CaseTimelineRepository;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCaseTimelineRepository implements CaseTimelineRepository {
    private final ConcurrentMap<UUID, CaseTimelineEntry> entriesById = new ConcurrentHashMap<>();

    @Override
    public CaseTimelineEntry save(CaseTimelineEntry entry) {
        entriesById.put(entry.id(), entry);
        return entry;
    }

    @Override
    public List<CaseTimelineEntry> findByCaseId(UUID caseId) {
        return entriesById.values().stream()
                .filter(entry -> entry.caseId().equals(caseId))
                .sorted(Comparator.comparing(CaseTimelineEntry::occurredAt))
                .toList();
    }
}
