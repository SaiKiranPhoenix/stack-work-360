package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.domain.PromotionReadinessSummary;
import java.math.BigDecimal;

public record PromotionReadinessSummaryResponse(
        String tenantId,
        String workerId,
        int reviewCount,
        BigDecimal averageRating,
        boolean ready,
        String recommendation
) {
    static PromotionReadinessSummaryResponse from(PromotionReadinessSummary summary) {
        return new PromotionReadinessSummaryResponse(summary.tenantId(), summary.workerId(), summary.reviewCount(), summary.averageRating(), summary.ready(), summary.recommendation());
    }
}
