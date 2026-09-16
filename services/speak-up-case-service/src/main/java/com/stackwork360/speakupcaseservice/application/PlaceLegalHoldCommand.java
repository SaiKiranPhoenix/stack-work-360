package com.stackwork360.speakupcaseservice.application;

public record PlaceLegalHoldCommand(
        String actorId,
        String legalMatterReference,
        String reason
) {
}
