package com.stackwork360.speakupcaseservice.application;

import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseStatus;

public record UpdateCaseStatusCommand(
        String actorId,
        SpeakUpCaseStatus status,
        String reason
) {
}
