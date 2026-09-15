package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.application.UpdateRepositoryCommand;
import com.stackwork360.developerintelligenceservice.domain.RepositoryLifecycle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateRepositoryRequest(
        @NotBlank String name,
        @NotBlank String defaultBranch,
        @NotNull RepositoryLifecycle lifecycle,
        @NotBlank String serviceName,
        @NotBlank String owningTeam,
        List<@NotBlank String> primaryMaintainers,
        List<@Valid CodeAreaRequest> codeAreas
) {
    UpdateRepositoryCommand toCommand() {
        return new UpdateRepositoryCommand(
                name,
                defaultBranch,
                lifecycle,
                serviceName,
                owningTeam,
                primaryMaintainers == null ? List.of() : primaryMaintainers,
                codeAreas == null ? List.of() : codeAreas.stream().map(CodeAreaRequest::toCommand).toList()
        );
    }
}
