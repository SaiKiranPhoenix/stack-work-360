package com.stackwork360.speakupcaseservice.api;

import com.stackwork360.speakupcaseservice.domain.EvidenceMetadata;
import java.time.Instant;
import java.util.UUID;

public record EvidenceMetadataResponse(
        UUID id,
        String fileName,
        String contentType,
        long sizeBytes,
        String storageReference,
        String uploadedBy,
        Instant uploadedAt
) {
    public static EvidenceMetadataResponse from(EvidenceMetadata metadata) {
        return new EvidenceMetadataResponse(
                metadata.id(),
                metadata.fileName(),
                metadata.contentType(),
                metadata.sizeBytes(),
                metadata.storageReference(),
                metadata.uploadedBy(),
                metadata.uploadedAt()
        );
    }
}
