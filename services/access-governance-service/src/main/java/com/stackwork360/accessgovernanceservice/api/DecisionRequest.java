package com.stackwork360.accessgovernanceservice.api;

import com.stackwork360.accessgovernanceservice.application.DecisionCommand;
import jakarta.validation.constraints.NotBlank;

public record DecisionRequest(@NotBlank String reason) {
    DecisionCommand toCommand() {
        return new DecisionCommand(reason);
    }
}
