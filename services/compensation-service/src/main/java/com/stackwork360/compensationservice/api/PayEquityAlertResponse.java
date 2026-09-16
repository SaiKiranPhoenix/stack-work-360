package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.domain.PayEquityAlert;
import java.util.UUID;

public record PayEquityAlertResponse(
        UUID id,
        String workerId,
        String message,
        MoneyResponse salary,
        MoneyResponse bandMinimum,
        MoneyResponse bandMaximum,
        MoneyResponse marketMedian
) {
    static PayEquityAlertResponse from(PayEquityAlert alert) {
        return new PayEquityAlertResponse(
                alert.id(),
                alert.workerId(),
                alert.message(),
                MoneyResponse.from(alert.salary()),
                MoneyResponse.from(alert.bandMinimum()),
                MoneyResponse.from(alert.bandMaximum()),
                MoneyResponse.from(alert.marketMedian())
        );
    }
}
