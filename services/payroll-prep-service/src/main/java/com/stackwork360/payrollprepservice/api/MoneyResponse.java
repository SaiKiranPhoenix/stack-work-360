package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.domain.Money;
import java.math.BigDecimal;

public record MoneyResponse(
        BigDecimal amount,
        String currency
) {
    static MoneyResponse from(Money money) {
        return new MoneyResponse(money.amount(), money.currency().getCurrencyCode());
    }
}
