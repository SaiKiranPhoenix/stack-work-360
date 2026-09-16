package com.stackwork360.compensationservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {
    public Money {
        Objects.requireNonNull(amount, "amount is required");
        currency = Objects.requireNonNull(currency, "currency is required");
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public Money plus(Money other) {
        ensureSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money minus(Money other) {
        ensureSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public boolean between(Money minimum, Money maximum) {
        ensureSameCurrency(minimum);
        ensureSameCurrency(maximum);
        return compareTo(minimum) >= 0 && compareTo(maximum) <= 0;
    }

    @Override
    public int compareTo(Money other) {
        ensureSameCurrency(other);
        return amount.compareTo(other.amount);
    }

    private void ensureSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("currency mismatch");
        }
    }
}
