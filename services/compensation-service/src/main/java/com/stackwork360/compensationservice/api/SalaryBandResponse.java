package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.domain.SalaryBand;
import java.util.UUID;

public record SalaryBandResponse(
        UUID id,
        String tenantId,
        String jobLevel,
        String location,
        MoneyResponse minimum,
        MoneyResponse midpoint,
        MoneyResponse maximum
) {
    static SalaryBandResponse from(SalaryBand band) {
        return new SalaryBandResponse(
                band.id(),
                band.tenantId(),
                band.jobLevel(),
                band.location(),
                MoneyResponse.from(band.minimum()),
                MoneyResponse.from(band.midpoint()),
                MoneyResponse.from(band.maximum())
        );
    }
}
