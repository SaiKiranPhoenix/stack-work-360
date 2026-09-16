package com.stackwork360.compensationservice.domain;

import java.util.UUID;

public record PayEquityAlert(
        UUID id,
        String workerId,
        String message,
        Money salary,
        Money bandMinimum,
        Money bandMaximum,
        Money marketMedian
) {
    public static PayEquityAlert belowBand(String workerId, Money salary, SalaryBand band, Money marketMedian) {
        return new PayEquityAlert(UUID.randomUUID(), workerId, "salary is below band minimum", salary, band.minimum(), band.maximum(), marketMedian);
    }

    public static PayEquityAlert aboveBand(String workerId, Money salary, SalaryBand band, Money marketMedian) {
        return new PayEquityAlert(UUID.randomUUID(), workerId, "salary is above band maximum", salary, band.minimum(), band.maximum(), marketMedian);
    }

    public static PayEquityAlert belowMarket(String workerId, Money salary, SalaryBand band, Money marketMedian) {
        return new PayEquityAlert(UUID.randomUUID(), workerId, "salary is more than 10 percent below market median", salary, band.minimum(), band.maximum(), marketMedian);
    }
}
