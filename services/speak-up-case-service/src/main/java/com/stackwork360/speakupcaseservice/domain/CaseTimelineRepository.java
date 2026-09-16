package com.stackwork360.speakupcaseservice.domain;

import java.util.List;
import java.util.UUID;

public interface CaseTimelineRepository {
    CaseTimelineEntry save(CaseTimelineEntry entry);
    List<CaseTimelineEntry> findByCaseId(UUID caseId);
}
