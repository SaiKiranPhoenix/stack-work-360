package com.stackwork360.speakupcaseservice.application;

public record AddEvidenceCommand(
        String actorId,
        String fileName,
        String contentType,
        long sizeBytes,
        String storageReference,
        String note
) {
}
