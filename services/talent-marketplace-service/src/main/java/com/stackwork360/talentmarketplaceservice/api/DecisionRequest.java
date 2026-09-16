package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.application.DecisionCommand;
import jakarta.validation.constraints.NotBlank;

public record DecisionRequest(@NotBlank String reason) {
    DecisionCommand toCommand() {
        return new DecisionCommand(reason);
    }
}
