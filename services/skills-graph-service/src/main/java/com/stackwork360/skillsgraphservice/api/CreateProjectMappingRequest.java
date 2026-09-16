package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.application.CreateProjectMappingCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateProjectMappingRequest(
        @NotBlank String projectCode,
        @NotEmpty List<String> skillCodes
) {
    CreateProjectMappingCommand toCommand(String tenantId) {
        return new CreateProjectMappingCommand(tenantId, projectCode, skillCodes);
    }
}
