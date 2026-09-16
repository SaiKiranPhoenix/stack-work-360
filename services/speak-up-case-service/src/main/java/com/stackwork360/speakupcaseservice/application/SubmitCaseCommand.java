package com.stackwork360.speakupcaseservice.application;

import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseCategory;

public record SubmitCaseCommand(
        String tenantId,
        String reporterIdentityHash,
        SpeakUpCaseCategory category,
        String subject,
        String description
) {
}
