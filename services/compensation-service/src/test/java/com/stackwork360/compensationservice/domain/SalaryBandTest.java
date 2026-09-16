package com.stackwork360.compensationservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SalaryBandTest {
    @Test
    void containsSalaryInsideBand() {
        SalaryBand band = new SalaryBand(null, "tenant-1", "L4", "IN", Money.of("100000", "USD"), Money.of("130000", "USD"), Money.of("160000", "USD"));

        assertThat(band.contains(Money.of("125000", "USD"))).isTrue();
    }

    @Test
    void rejectsInvalidBandOrdering() {
        assertThatThrownBy(() -> new SalaryBand(null, "tenant-1", "L4", "IN", Money.of("140000", "USD"), Money.of("130000", "USD"), Money.of("160000", "USD")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("salary band");
    }
}
