package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.workforceplanningservice.application.CreateHeadcountPlanCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateHeadcountPlanRequest(
        @NotBlank String teamId,
        @NotBlank String location,
        @Min(0) int currentFullTime,
        @Min(0) int currentContractors,
        @Min(0) int targetFullTime,
        @Min(0) int targetContractors,
        @NotNull BigDecimal averageFullTimeCost,
        @NotNull BigDecimal averageContractorCost
) {
    CreateHeadcountPlanCommand toCommand(String tenantId) {
        return new CreateHeadcountPlanCommand(tenantId, teamId, location, currentFullTime, currentContractors, targetFullTime, targetContractors, averageFullTimeCost, averageContractorCost);
    }
}
