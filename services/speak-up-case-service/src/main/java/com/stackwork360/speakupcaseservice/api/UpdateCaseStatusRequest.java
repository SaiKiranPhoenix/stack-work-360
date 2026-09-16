package com.stackwork360.speakupcaseservice.api;

import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCaseStatusRequest(
        @NotBlank String actorId,
        @NotNull SpeakUpCaseStatus status,
        @NotBlank String reason
) {
}
