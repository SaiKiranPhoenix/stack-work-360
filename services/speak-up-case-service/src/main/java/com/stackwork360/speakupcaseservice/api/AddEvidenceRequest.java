package com.stackwork360.speakupcaseservice.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddEvidenceRequest(
        @NotBlank String actorId,
        @NotBlank String fileName,
        @NotBlank String contentType,
        @Positive long sizeBytes,
        @NotBlank String storageReference,
        @NotBlank String note
) {
}
