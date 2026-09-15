package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.application.CodeAreaCommand;
import jakarta.validation.constraints.NotBlank;

public record CodeAreaRequest(
        @NotBlank String pathPattern,
        @NotBlank String ownerGroup
) {
    CodeAreaCommand toCommand() {
        return new CodeAreaCommand(pathPattern, ownerGroup);
    }
}
