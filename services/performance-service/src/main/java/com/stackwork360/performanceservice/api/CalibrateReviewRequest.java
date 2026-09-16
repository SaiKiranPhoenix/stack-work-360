package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.application.CalibrateReviewCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CalibrateReviewRequest(
        @Min(1) @Max(5) int calibratedRating,
        @NotBlank String notes
) {
    CalibrateReviewCommand toCommand() {
        return new CalibrateReviewCommand(calibratedRating, notes);
    }
}
