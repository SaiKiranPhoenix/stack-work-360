package com.stackwork360.speakupcaseservice.application;

public record AssignInvestigatorCommand(
        String actorId,
        String investigatorId,
        String reason
) {
}
