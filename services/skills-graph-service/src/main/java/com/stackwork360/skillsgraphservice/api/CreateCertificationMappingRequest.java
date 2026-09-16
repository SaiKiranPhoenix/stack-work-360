package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.application.CreateCertificationMappingCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateCertificationMappingRequest(
        @NotBlank String certificationCode,
        @NotEmpty List<String> skillCodes
) {
    CreateCertificationMappingCommand toCommand(String tenantId) {
        return new CreateCertificationMappingCommand(tenantId, certificationCode, skillCodes);
    }
}
