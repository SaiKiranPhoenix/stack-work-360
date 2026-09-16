package com.stackwork360.skillsgraphservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SuccessionCoverageRequest(
        @NotBlank String roleName,
        @Valid @NotEmpty List<RequiredSkillRequest> requiredSkills
) {
}
