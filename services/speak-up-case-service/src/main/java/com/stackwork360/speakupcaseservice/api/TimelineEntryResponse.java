package com.stackwork360.speakupcaseservice.api;

import com.stackwork360.speakupcaseservice.domain.CaseTimelineAction;
import com.stackwork360.speakupcaseservice.domain.CaseTimelineEntry;
import java.time.Instant;
import java.util.UUID;

public record TimelineEntryResponse(
        UUID id,
        UUID caseId,
        String actorId,
        CaseTimelineAction action,
        String note,
        Instant occurredAt
) {
    public static TimelineEntryResponse from(CaseTimelineEntry entry) {
        return new TimelineEntryResponse(entry.id(), entry.caseId(), entry.actorId(), entry.action(), entry.note(), entry.occurredAt());
    }
}
