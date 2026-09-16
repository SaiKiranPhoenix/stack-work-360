package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.Objective;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ObjectiveRequest(
        @NotBlank String title,
        @Valid @NotEmpty List<KeyResultRequest> keyResults
) {
    Objective toDomain() {
        return new Objective(null, title, keyResults.stream().map(KeyResultRequest::toDomain).toList());
    }
}
