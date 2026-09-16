package com.stackwork360.speakupcaseservice.api;

import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmitCaseRequest(
        @NotBlank String reporterIdentityHash,
        @NotNull SpeakUpCaseCategory category,
        @NotBlank String subject,
        @NotBlank String description
) {
}
