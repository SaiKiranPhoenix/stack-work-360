package com.stackwork360.speakupcaseservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpeakUpCaseRepository {
    SpeakUpCase save(SpeakUpCase speakUpCase);
    Optional<SpeakUpCase> findById(UUID id);
    List<SpeakUpCase> findByTenantId(String tenantId);
}
