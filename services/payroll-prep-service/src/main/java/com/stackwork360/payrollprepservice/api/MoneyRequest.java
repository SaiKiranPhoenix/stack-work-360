package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.domain.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MoneyRequest(
        @NotNull BigDecimal amount,
        @NotBlank String currency
) {
    Money toDomain() {
        return Money.of(amount.toPlainString(), currency);
    }
}
