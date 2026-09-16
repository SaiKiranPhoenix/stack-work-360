package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.domain.Money;
import java.math.BigDecimal;

public record MoneyResponse(
        BigDecimal amount,
        String currency
) {
    static MoneyResponse from(Money money) {
        return new MoneyResponse(money.amount(), money.currency().getCurrencyCode());
    }
}
