package com.stackwork360.developerintelligenceservice.api;

import com.stackwork360.developerintelligenceservice.application.RegisterRepositoryCommand;
import com.stackwork360.developerintelligenceservice.domain.RepositoryProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RegisterRepositoryRequest(
        @NotNull RepositoryProvider provider,
        @NotBlank String externalId,
        @NotBlank String name,
        @NotBlank String defaultBranch,
        @NotBlank String serviceName,
        @NotBlank String owningTeam,
        List<@NotBlank String> primaryMaintainers,
        List<@Valid CodeAreaRequest> codeAreas
) {
    RegisterRepositoryCommand toCommand(String tenantId) {
        return new RegisterRepositoryCommand(
                tenantId,
                provider,
                externalId,
                name,
                defaultBranch,
                serviceName,
                owningTeam,
                primaryMaintainers == null ? List.of() : primaryMaintainers,
                codeAreas == null ? List.of() : codeAreas.stream().map(CodeAreaRequest::toCommand).toList()
        );
    }
}
