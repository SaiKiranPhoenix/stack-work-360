package com.stackwork360.performanceservice.domain;

import java.math.BigDecimal;

public record PromotionReadinessSummary(
        String tenantId,
        String workerId,
        int reviewCount,
        BigDecimal averageRating,
        boolean ready,
        String recommendation
) {
}
